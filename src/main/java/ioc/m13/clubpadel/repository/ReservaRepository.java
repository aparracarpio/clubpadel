package ioc.m13.clubpadel.repository;

import ioc.m13.clubpadel.model.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    // Reservas de un usuario, cargando usuario y pista en la misma query
    @Query("SELECT r FROM Reserva r " +
           "JOIN FETCH r.usuario " +
           "JOIN FETCH r.pista " +
           "WHERE r.usuario.id = :idUsuario")
    List<Reserva> findByUsuarioIdConRelaciones(@Param("idUsuario") Long idUsuario);

    // Todas las reservas con usuario y pista cargados
    @Query("SELECT r FROM Reserva r JOIN FETCH r.usuario JOIN FETCH r.pista")
    List<Reserva> findAllConRelaciones();

    
    List<Reserva> findByUsuarioId(Long usuarioId);
    List<Reserva> findByPistaIdAndFecha(Long pistaId, LocalDate fecha);
    List<Reserva> findByPistaIdAndFechaAndEstado(Long pistaId, LocalDate fecha, String estado);
}