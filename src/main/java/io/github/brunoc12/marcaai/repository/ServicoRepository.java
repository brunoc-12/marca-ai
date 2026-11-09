package io.github.brunoc12.marcaai.repository;



import io.github.brunoc12.marcaai.model.Servico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ServicoRepository extends JpaRepository<Servico, Long> {
    Optional<Servico> findByNome(String nome);

}
