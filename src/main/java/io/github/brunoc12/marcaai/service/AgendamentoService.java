
package io.github.brunoc12.marcaai.service;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import io.github.brunoc12.marcaai.model.Agendamento;
import io.github.brunoc12.marcaai.model.Cliente;
import io.github.brunoc12.marcaai.model.Profissional;
import io.github.brunoc12.marcaai.model.Servico;
import io.github.brunoc12.marcaai.model.StatusAgendamento;

import io.github.brunoc12.marcaai.repository.AgendamentoRepository;
import io.github.brunoc12.marcaai.repository.ClienteRepository;
import io.github.brunoc12.marcaai.repository.ProfissionalRepository;
import io.github.brunoc12.marcaai.repository.ServicoRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AgendamentoService {

    private final AgendamentoRepository agendamentoRepository;
    private final ClienteRepository clienteRepository;
    private final ProfissionalRepository profissionalRepository;
    private final ServicoRepository servicoRepository;
    private final GoogleCalendarService googleCalendarService;

    public AgendamentoService(
            AgendamentoRepository agendamentoRepository,
            ClienteRepository clienteRepository,
            ProfissionalRepository profissionalRepository,
            ServicoRepository servicoRepository,
            GoogleCalendarService googleCalendarService) {

        this.agendamentoRepository = agendamentoRepository;
        this.clienteRepository = clienteRepository;
        this.profissionalRepository = profissionalRepository;
        this.servicoRepository = servicoRepository;
        this.googleCalendarService = googleCalendarService;
    }

    // SALVAR AGENDAMENTO
    @Transactional
    public Agendamento salvar(Agendamento agendamento) {

        Cliente cliente = clienteRepository.findById(
                agendamento.getCliente().getId()
        ).orElseThrow(() ->
                new RuntimeException("Cliente não encontrado!"));

        Profissional profissional = profissionalRepository.findById(
                agendamento.getProfissional().getId()
        ).orElseThrow(() ->
                new RuntimeException("Profissional não encontrado!"));

        Servico servico = servicoRepository.findById(
                agendamento.getServico().getId()
        ).orElseThrow(() ->
                new RuntimeException("Serviço não encontrado!"));

        boolean fazOServico = profissional.getServicos().stream()
                .anyMatch(s -> s.getId().equals(servico.getId()));

        if (!fazOServico) {
            throw new RuntimeException(
                    "Esse profissional não realiza esse serviço!");
        }

        LocalDateTime inicio = agendamento.getDataHoraInicio();

        LocalDateTime fim = inicio.plusMinutes(
                servico.getDuracaoMinutos()
        );

        List<Agendamento> conflitos = agendamentoRepository
                .findByProfissional_IdAndStatusNotAndDataHoraInicioLessThanAndDataHoraFimGreaterThan(
                        profissional.getId(),
                        StatusAgendamento.CANCELADO,
                        fim,
                        inicio
                );

        if (!conflitos.isEmpty()) {
            throw new RuntimeException(
                    "O profissional já tem um agendamento nesse horário!");
        }

        agendamento.setCliente(cliente);
        agendamento.setProfissional(profissional);
        agendamento.setServico(servico);
        agendamento.setDataHoraFim(fim);

        agendamento.setStatusAgendamento(
                StatusAgendamento.AGENDADO
        );

        agendamento.setStatus(
                StatusAgendamento.AGENDADO
        );

        // Primeiro salva no Oracle
        Agendamento agendamentoSalvo =
                agendamentoRepository.save(agendamento);

        try {

            // Cria o compromisso no Google Calendar
            var evento = googleCalendarService.criarEvento(

                    "Agendamento - " + servico.getNome(),

                    "Cliente: " + cliente.getNome()
                            + "\nProfissional: " + profissional.getNome()
                            + "\nServiço: " + servico.getNome(),

                    inicio,
                    fim
            );

            // Guarda o ID retornado pelo Google
            agendamentoSalvo.setGoogleEventId(evento.getId());

            // Atualiza o registro no Oracle
            return agendamentoRepository.save(agendamentoSalvo);

        } catch (IOException | GeneralSecurityException e) {

            throw new IllegalStateException(
                    "Não foi possível sincronizar com Google Calendar.",
                    e
            );
        }
    }

    // BUSCAR POR ID
    public Agendamento buscarPorId(Long id) {

        return agendamentoRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Agendamento não encontrado!"
                        )
                );
    }

    // BUSCAR TODOS
    public List<Agendamento> buscarTodos() {
        return agendamentoRepository.findAll();
    }


    // EXCLUIR AGENDAMENTO
    @Transactional
    public void excluir(Long id) {

        Agendamento agendamento = agendamentoRepository
                .findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Agendamento não encontrado!"
                        )
                );

        String googleEventId = agendamento.getGoogleEventId();

        try {

            // Exclui o evento do Google, caso exista
            if (googleEventId != null && !googleEventId.isBlank()) {

                googleCalendarService.deletarEvento(googleEventId);
            }

            // Depois exclui o registro do Oracle
            agendamentoRepository.delete(agendamento);

        } catch (IOException | GeneralSecurityException e) {

            throw new IllegalStateException(
                    "Não foi possível excluir o evento do Google Calendar.",
                    e
            );
        }
    }


    // ATUALIZAR
    @Transactional
    public Agendamento atualizar(Long id, Agendamento agendamento) {

        Agendamento agendamentoExistente = buscarPorId(id);

        if (agendamentoExistente.getStatus()
                != StatusAgendamento.AGENDADO) {

            throw new RuntimeException(
                    "Só é possível remarcar agendamentos com status AGENDADO!"
            );
        }

        LocalDateTime novoInicio =
                agendamento.getDataHoraInicio();

        LocalDateTime novoFim = novoInicio.plusMinutes(
                agendamentoExistente.getServico().getDuracaoMinutos()
        );

        List<Agendamento> conflitos = agendamentoRepository
                .findByProfissional_IdAndStatusNotAndDataHoraInicioLessThanAndDataHoraFimGreaterThan(
                        agendamentoExistente.getProfissional().getId(),
                        StatusAgendamento.CANCELADO,
                        novoFim,
                        novoInicio
                );

        boolean existeConflito = conflitos.stream()
                .anyMatch(c -> !c.getId().equals(id));

        if (existeConflito) {
            throw new RuntimeException(
                    "O profissional já tem um agendamento nesse horário!"
            );
        }

        agendamentoExistente.setDataHoraInicio(novoInicio);
        agendamentoExistente.setDataHoraFim(novoFim);

        return agendamentoRepository.save(agendamentoExistente);
    }
}
