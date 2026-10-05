package ioc.m13.clubpadel.repository;

import ioc.m13.clubpadel.model.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    // Reservas de un usuario concreto
    List<Reserva> findByUsuarioId(Long usuarioId);

    // Reservas de una pista en una fecha concreta
    List<Reserva> findByPistaIdAndFecha(Long pistaId, LocalDate fecha);

    // Reservas activas de una pista en una fecha (para comprobar solapamiento)
    List<Reserva> findByPistaIdAndFechaAndEstado(Long pistaId, LocalDate fecha, String estado);
}