package ioc.m13.clubpadel.controller;

import ioc.m13.clubpadel.dto.UsuarioResponse;
import ioc.m13.clubpadel.repository.UsuarioRepository;
import ioc.m13.clubpadel.service.ReservaService;
import ioc.m13.clubpadel.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired private UsuarioService usuarioService;
    @Autowired private ReservaService reservaService;
    @Autowired private UsuarioRepository usuarioRepository;

    // GET /api/admin/verificar/{email} → datos del usuario + sus reservas (ADMIN)
    @GetMapping("/verificar/{email}")
    public ResponseEntity<?> verificar(@PathVariable String email) {
        try {
            UsuarioResponse usuario = usuarioService.buscarPorEmail(email);
            var reservas = usuarioRepository.findByEmail(email)
                    .map(u -> reservaService.misReservas(u.getEmail()))
                    .orElse(List.of());
            return ResponseEntity.ok(Map.of(
                    "usuario", usuario,
                    "reservas", reservas
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}