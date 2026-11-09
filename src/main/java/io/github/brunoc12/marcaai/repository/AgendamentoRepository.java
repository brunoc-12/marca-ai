package io.github.brunoc12.marcaai.repository;



import io.github.brunoc12.marcaai.model.Agendamento;
import io.github.brunoc12.marcaai.model.StatusAgendamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {
    List<Agendamento> findByProfissional_IdAndStatusNotAndDataHoraInicioLessThanAndDataHoraFimGreaterThan(
            Long profissionalId,
            StatusAgendamento status,
            LocalDateTime fim,
            LocalDateTime inicio);

}
