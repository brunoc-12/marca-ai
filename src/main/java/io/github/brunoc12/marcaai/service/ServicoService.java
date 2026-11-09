package io.github.brunoc12.marcaai.service;




import io.github.brunoc12.marcaai.model.Servico;
import io.github.brunoc12.marcaai.repository.ServicoRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ServicoService {

    private final ServicoRepository servicoRepository;


    public ServicoService(ServicoRepository servicoRepository) {
        this.servicoRepository = servicoRepository;
    }


    @Transactional
    public Servico salvar(Servico servico) {
        Optional<Servico> existente = servicoRepository.findByNome(servico.getNome());

        if (existente.isPresent()) {
            throw new RuntimeException("Já existe um Serviço com esse Nome!");
        }

        return servicoRepository.save(servico);
    }



    public Servico buscarPorId(Long id) {
        Optional<Servico> servico = servicoRepository.findById(id);

        if (servico.isPresent()) {
            return servico.get();
        } else {
            throw new RuntimeException("Serviço não encontrado!");
        }
    }


    public List<Servico> buscarTodos(){
        return servicoRepository.findAll();
    }


    public void excluir(Long id) {
        Optional<Servico> servico = servicoRepository.findById(id);
        if (servico.isPresent()) {
           servicoRepository.deleteById(id);
        } else {
            throw new RuntimeException("Serviço não encontrado!");
        }

    }

    public Servico atualizar(Long id, Servico servico) {

        Optional<Servico> servicoAtual = servicoRepository.findById(id);

        if (servicoAtual.isPresent()) {
            Servico servicoExistente = servicoAtual.get();
            servicoExistente.setNome(servico.getNome());
            servicoExistente.setDuracaoMinutos(servico.getDuracaoMinutos());
            servicoExistente.setPreco(servico.getPreco());
            return servicoRepository.save(servicoExistente);
        } else {
            throw new RuntimeException("Serviço não encontrado!");
        }
    }

}
