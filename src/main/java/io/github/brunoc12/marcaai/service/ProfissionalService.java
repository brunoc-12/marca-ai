

package io.github.brunoc12.marcaai.service;

import io.github.brunoc12.marcaai.model.Profissional;
import io.github.brunoc12.marcaai.model.Servico;
import io.github.brunoc12.marcaai.repository.ProfissionalRepository;
import io.github.brunoc12.marcaai.repository.ServicoRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

    @Service
    public class ProfissionalService {

        private final ProfissionalRepository profissionalRepository;
        private final ServicoRepository servicoRepository;

        public ProfissionalService(ProfissionalRepository profissionalRepository,
                                   ServicoRepository servicoRepository) {
            this.profissionalRepository = profissionalRepository;
            this.servicoRepository = servicoRepository;
        }

        @Transactional
        public Profissional salvar(Profissional profissional) {
            profissional.setServicos(buscarServicosExistentes(profissional.getServicos()));
            return profissionalRepository.save(profissional);
        }

        public Profissional buscarPorId(Long id) {
            Optional<Profissional> profissional = profissionalRepository.findById(id);

            if (profissional.isPresent()) {
                return profissional.get();
            } else {
                throw new RuntimeException("Profissional não encontrado!");
            }
        }

        public List<Profissional> buscarTodos() {
            return profissionalRepository.findAll();
        }

        public void excluir(Long id) {
            Optional<Profissional> profissional = profissionalRepository.findById(id);

            if (profissional.isPresent()) {
                profissionalRepository.deleteById(id);
            } else {
                throw new RuntimeException("Profissional não encontrado!");
            }
        }

        @Transactional
        public Profissional atualizar(Long id, Profissional profissional) {
            Optional<Profissional> profissionalAtual = profissionalRepository.findById(id);

            if (profissionalAtual.isPresent()) {
                Profissional profissionalExistente = profissionalAtual.get();
                profissionalExistente.setNome(profissional.getNome());
                profissionalExistente.setServicos(buscarServicosExistentes(profissional.getServicos()));
                return profissionalRepository.save(profissionalExistente);
            } else {
                throw new RuntimeException("Profissional não encontrado!");
            }
        }

        private Set<Servico> buscarServicosExistentes(Set<Servico> servicosRecebidos) {
            Set<Servico> servicosExistentes = new HashSet<>();

            for (Servico servico : servicosRecebidos) {
                Optional<Servico> encontrado = servicoRepository.findById(servico.getId());

                if (encontrado.isPresent()) {
                    servicosExistentes.add(encontrado.get());
                } else {
                    throw new RuntimeException("Serviço não encontrado!");
                }
            }

            return servicosExistentes;
        }
    }
