package ioc.m13.clubpadel.controller;

import ioc.m13.clubpadel.dto.ReservaRequest;
import ioc.m13.clubpadel.dto.ReservaResponse;
import ioc.m13.clubpadel.service.ReservaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reservas")
public class ReservaController {

    @Autowired
    private ReservaService reservaService;

    // POST /api/reservas → crear reserva (USER/ADMIN)
    @PostMapping
    public ResponseEntity<?> crear(@RequestBody ReservaRequest req, Authentication auth) {
        try {
            return ResponseEntity.ok(reservaService.crear(auth.getName(), req));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // GET /api/reservas/mis-reservas → reservas del usuario logueado
    @GetMapping("/mis-reservas")
    public List<ReservaResponse> misReservas(Authentication auth) {
        return reservaService.misReservas(auth.getName());
    }

    // DELETE /api/reservas/{id} → cancelar reserva (USER/ADMIN)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> cancelar(@PathVariable Long id, Authentication auth) {
        try {
            boolean esAdmin = auth.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
            reservaService.cancelar(id, auth.getName(), esAdmin);
            return ResponseEntity.ok(Map.of("mensaje", "Reserva cancelada"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // GET /api/reservas → todas las reservas (ADMIN)
    @GetMapping
    public List<ReservaResponse> todas() {
        return reservaService.todas();
    }
}