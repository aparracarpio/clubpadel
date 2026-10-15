package ioc.m13.clubpadel.service;

import ioc.m13.clubpadel.dto.ReservaRequest;
import ioc.m13.clubpadel.dto.ReservaResponse;
import ioc.m13.clubpadel.model.Pista;
import ioc.m13.clubpadel.model.Reserva;
import ioc.m13.clubpadel.model.Usuario;
import ioc.m13.clubpadel.repository.PistaRepository;
import ioc.m13.clubpadel.repository.ReservaRepository;
import ioc.m13.clubpadel.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
/**
 * ReservaService, servicio de logica de Reserva. 
 * Implementa:
 *      - Creacion de reservas
 *      - Listado de reservas propias y todas las reservas (ADMIN)
 *      - Cancelación de reserva 
 */
@Service
public class ReservaService {
    /*Repositorio Reserva */
    @Autowired private ReservaRepository reservaRepository;
    /*Repositorio Usuario */
    @Autowired private UsuarioRepository usuarioRepository;
    /*Repositorio Pista */
    @Autowired private PistaRepository pistaRepository;
    /**
     * Crea una reserva para el usuario autenticado.
     * @param emailUsuario correo usuario
     * @param req DTO (Pista) con idPista, fecha y horas
     * @return ReservaResponse con la reserva creada
     * @throws RuntimeException si falla alguna validación
     */
    @Transactional
    public ReservaResponse crear(String emailUsuario, ReservaRequest req) {
        // Comprobar usuario existente
        Usuario usuario = usuarioRepository.findByEmail(emailUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        // Comprobar pista existente
        Pista pista = pistaRepository.findById(req.getIdPista())
                .orElseThrow(() -> new RuntimeException("Pista no encontrada"));
        // Validar coherencia horaria
        if (req.getHoraFin() == null || req.getHoraInicio() == null
                || !req.getHoraFin().isAfter(req.getHoraInicio())) {
            throw new RuntimeException("La hora de fin debe ser posterior a la de inicio");
        }
        // Comprobar que no exista una reserva con las mismas caracteristicas para la misma pista
        List<Reserva> existentes = reservaRepository
                .findByPistaIdAndFechaAndEstado(pista.getId(), req.getFecha(), "Activa");
        
        for (Reserva r : existentes) {
            boolean solapa = req.getHoraInicio().isBefore(r.getHoraFin())
                          && req.getHoraFin().isAfter(r.getHoraInicio());
            if (solapa) {
                throw new RuntimeException("La pista ya está reservada en ese horario");
            }
        }
        // Construir y guardar reserva pista
        Reserva reserva = new Reserva();
        reserva.setUsuario(usuario);
        reserva.setPista(pista);
        reserva.setFecha(req.getFecha());
        reserva.setHoraInicio(req.getHoraInicio());
        reserva.setHoraFin(req.getHoraFin());
        reserva.setEstado("Activa");

        return toResponse(reservaRepository.save(reserva));
    }
    /**
     * Devuelve reservas del usuario autenticado
     * @param emailUsuario correo usuario
     * @return lista de reservas propias
     */
    @Transactional(readOnly = true)
    public List<ReservaResponse> misReservas(String emailUsuario) {
        Usuario usuario = usuarioRepository.findByEmail(emailUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return reservaRepository.findByUsuarioId(usuario.getId())
                .stream().map(this::toResponse).toList();
    }
    /**
     * Cancela una reserva (Pasa estado reserva a cancelada). Un usuario solo puede cancelar sus propias reservas.
     * @param idReserva identificador reserva
     * @param emailUsuario correo usuario
     * @param esAdmin true si es admin, false sino.
     * @throws RuntimeException si reserva no existe o usuario no es propietario
     */
    @Transactional
    public void cancelar(Long idReserva, String emailUsuario, boolean esAdmin) {
        Reserva r = reservaRepository.findById(idReserva)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));

        if (!esAdmin && !r.getUsuario().getEmail().equals(emailUsuario)) {
            throw new RuntimeException("No puedes cancelar esta reserva");
        }

        r.setEstado("Cancelada");
        reservaRepository.save(r);
    }
    /**
     * Lista todas las reservas (Solo ADMIN)
     * @return lista completa con todas las reservas de todos los usuarios
     */
    @Transactional(readOnly = true)
    public List<ReservaResponse> todas() {
        return reservaRepository.findAll().stream().map(this::toResponse).toList();
    }
    /**
     * Mapea entidad Reserva a un DTO con datos.
     * @param r
     * @return
     */
    private ReservaResponse toResponse(Reserva r) {
        return new ReservaResponse(
                r.getId(),
                r.getUsuario().getId(),
                r.getUsuario().getNombre(),
                r.getPista().getId(),
                r.getPista().getNombre(),
                r.getFecha(),
                r.getHoraInicio(),
                r.getHoraFin(),
                r.getEstado()
        );
    }
}