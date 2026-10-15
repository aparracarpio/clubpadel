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

/**
 * 
 * ReservaController, controlador REST para recurso Reserva.
 *        Ruta base: /api/reservas
 *        Metodos: POST, GET, DELETE
 *                  POST → crear reserva (USER/ADMIN)
 *                  GET → todas las reservas (ADMIN)
 *                  DELETE → cancelar reserva (USER/ADMIN)
 */

@RestController
@RequestMapping("/api/reservas")
public class ReservaController {

    // Servicio con logica de negocio para reservas
    @Autowired
    private ReservaService reservaService;

    /**
     * POST /api/reservas → crear reserva (USER/ADMIN)
     * @param req DTO con datos de reserva (pista, fecha, hora)
     * @param auth información del usuario logueado (email)
     * @return 200 -> reserva creada correctamente
     *         400 -> error (pista no existe, pista ocupada, fecha/hora inválida, etc.)
     */
    @PostMapping
    public ResponseEntity<?> crear(@RequestBody ReservaRequest req, Authentication auth) {
        try {
            return ResponseEntity.ok(reservaService.crear(auth.getName(), req));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * GET /api/reservas/mis-reservas → reservas del usuario logueado
     * @param auth información del usuario logueado (email)
     * @return 200 -> lista reservas (ReservaResponse) con id, pista, fecha, hora
     *         400 -> error (usuario no existe, etc.)
     */
    @GetMapping("/mis-reservas")
    public List<ReservaResponse> misReservas(Authentication auth) {
        return reservaService.misReservas(auth.getName());
    }

    /**
     * DELETE /api/reservas/{id} → cancelar reserva (USER/ADMIN)
     * @param id identificador reserva
     * @param auth información del usuario logueado (email y roles)
     * @return 200 -> reserva cancelada correctamente
     *         400 -> error (reserva no existe, usuario no es dueño de la reserva, etc.)
     */
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

    /**
     * GET /api/reservas → devuelve lista de todas las reservas (solo ADMIN)
     * @return lista reservas (ReservaResponse) con id, pista, fecha, hora, usuario
     */
    @GetMapping
    public List<ReservaResponse> todas() {
        return reservaService.todas();
    }
}