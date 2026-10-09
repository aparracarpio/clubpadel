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

@Service
public class ReservaService {

    @Autowired private ReservaRepository reservaRepository;
    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private PistaRepository pistaRepository;

    @Transactional
    public ReservaResponse crear(String emailUsuario, ReservaRequest req) {
        Usuario usuario = usuarioRepository.findByEmail(emailUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Pista pista = pistaRepository.findById(req.getIdPista())
                .orElseThrow(() -> new RuntimeException("Pista no encontrada"));

        if (req.getHoraFin() == null || req.getHoraInicio() == null
                || !req.getHoraFin().isAfter(req.getHoraInicio())) {
            throw new RuntimeException("La hora de fin debe ser posterior a la de inicio");
        }

        List<Reserva> existentes = reservaRepository
                .findByPistaIdAndFechaAndEstado(pista.getId(), req.getFecha(), "Activa");

        for (Reserva r : existentes) {
            boolean solapa = req.getHoraInicio().isBefore(r.getHoraFin())
                          && req.getHoraFin().isAfter(r.getHoraInicio());
            if (solapa) {
                throw new RuntimeException("La pista ya está reservada en ese horario");
            }
        }

        Reserva reserva = new Reserva();
        reserva.setUsuario(usuario);
        reserva.setPista(pista);
        reserva.setFecha(req.getFecha());
        reserva.setHoraInicio(req.getHoraInicio());
        reserva.setHoraFin(req.getHoraFin());
        reserva.setEstado("Activa");

        return toResponse(reservaRepository.save(reserva));
    }

    @Transactional(readOnly = true)
    public List<ReservaResponse> misReservas(String emailUsuario) {
        Usuario usuario = usuarioRepository.findByEmail(emailUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return reservaRepository.findByUsuarioId(usuario.getId())
                .stream().map(this::toResponse).toList();
    }

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

    @Transactional(readOnly = true)
    public List<ReservaResponse> todas() {
        return reservaRepository.findAll().stream().map(this::toResponse).toList();
    }

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