package io.github.brunoc12.marcaai.service;


import io.github.brunoc12.marcaai.model.Cliente;
import io.github.brunoc12.marcaai.model.Profissional;
import io.github.brunoc12.marcaai.repository.ClienteRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Transactional
    public Cliente salvar(Cliente cliente) {
        Optional<Cliente> existente = clienteRepository.findByTelefone(cliente.getTelefone());

        if (existente.isPresent()) {
            throw new RuntimeException("Já existe um cliente com esse telefone!");
        }

        return clienteRepository.save(cliente);
    }

    public Cliente buscarPorId(Long id) {
        Optional<Cliente> profissional = clienteRepository.findById(id);

        if (profissional.isPresent()) {
            return profissional.get();
        } else {
            throw new RuntimeException("Cliente não encontrado!");
        }
    }


    public List<Cliente> buscarTodos(){
        return clienteRepository.findAll();
    }

    public void excluir(Long id) {
        Optional<Cliente> cliente = clienteRepository.findById(id);
        if (cliente.isPresent()) {
            clienteRepository.deleteById(id);
        } else {
            throw new RuntimeException("Cliente não encontrado!");
        }

    }
    public Cliente atualizar(Long id, Cliente cliente) {
        Optional<Cliente> clienteAtual = clienteRepository.findById(id);

        if (clienteAtual.isPresent()) {
            Cliente clienteExistente = clienteAtual.get();
            clienteExistente.setTelefone(cliente.getTelefone());
            clienteExistente.setNome(cliente.getNome());
            return clienteRepository.save(clienteExistente);
        } else {
            throw new RuntimeException("Cliente não encontrado!");
        }
    }
}



