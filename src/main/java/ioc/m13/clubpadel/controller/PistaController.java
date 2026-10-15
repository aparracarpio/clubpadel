package ioc.m13.clubpadel.controller;

import ioc.m13.clubpadel.dto.PistaRequest;
import ioc.m13.clubpadel.model.Pista;
import ioc.m13.clubpadel.service.PistaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 
 * PistaController
 * Controlador REST para recurso Pista.
 *        Ruta base: /api/pistas
 *        Metodos: GET, POST, PUT, DELETE
 *                  GET -> todas (público)
 *                  POST, PUT, DELETE -> solo ADMIN
 */

@RestController
@RequestMapping("/api/pistas")
public class PistaController {

          // Servicio con logica de negocio para pistas
          @Autowired
          private PistaService pistaService;

          /**
           * GET /api/pistas → devuelve lista de todas las pistas (público)
           * @return lista pistas (Pista) con id, nombre, estado
           */
          @GetMapping
          public List<Pista> getAll() {
                    return pistaService.listar();
          }

          /**
           * GET /api/pistas/disponibles → devuelve lista de pistas disponibles (público)
           * @return lista pistas (Pista) con id, nombre, estado
           */
          @GetMapping("/disponibles")
          public List<Pista> getDisponibles() {
                    return pistaService.listarPorEstado("Disponible");
          }

          /**
           * GET /api/pistas/ocupadas → devuelve lista de pistas ocupadas (público)
           * @return lista pistas (Pista) con id, nombre, estado
           */
          @GetMapping("/ocupadas")
          public List<Pista> getOcupadas() {
                    return pistaService.listarPorEstado("Ocupada");
          }

          /**
           * GET /api/pistas/{id} → devuelve una pista por su ID (público)
           * @param id identificador pista
           * @return pista (Pista) con id, nombre, estado
           */
          @GetMapping("/{id}")
          public ResponseEntity<?> getById(@PathVariable Long id) {
                    try {
                              return ResponseEntity.ok(pistaService.buscarPorId(id));
                    } catch (RuntimeException e) {
                              return ResponseEntity.notFound().build();
                    }
          }

          /**
           * POST /api/pistas → crear pista (solo ADMIN)
           * @param req DTO con datos de pista (nombre, estado)
           * @return pista creada (Pista) con id, nombre, estado
           */
          @PostMapping
          public Pista crear(@RequestBody PistaRequest req) {
                    return pistaService.crear(req);
          }

          /**
           * PUT /api/pistas/{id} → editar pista (solo ADMIN)
           * @param id identificador pista
           * @param req DTO con datos de pista (nombre, estado)
           * @return pista editada (Pista) con id, nombre, estado
           */
          @PutMapping("/{id}")
          public ResponseEntity<?> editar(@PathVariable Long id, @RequestBody PistaRequest req) {
                    try {
                              return ResponseEntity.ok(pistaService.editar(id, req));
                    } catch (RuntimeException e) {
                              return ResponseEntity.notFound().build();
                    }
          }

          /**
           * DELETE /api/pistas/{id} → eliminar pista (solo ADMIN)
           * @param id identificador pista
           * @return mensaje de confirmación
           */
          @DeleteMapping("/{id}")
          public ResponseEntity<?> eliminar(@PathVariable Long id) {
                    try {
                              pistaService.eliminar(id);
                              return ResponseEntity.ok(Map.of("mensaje", "Pista eliminada"));
                    } catch (RuntimeException e) {
                              return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
                    }
          }

          /**
           * PUT /api/pistas/{id}/ocupar → cambiar a Ocupada (ADMIN)
           * @param id identificador pista
           * @return pista editada (Pista) con id, nombre, estado
           */
          @PutMapping("/{id}/ocupar")
          public ResponseEntity<?> ocupar(@PathVariable Long id) {
                    try {
                              return ResponseEntity.ok(pistaService.ocupar(id));
                    } catch (RuntimeException e) {
                              return ResponseEntity.notFound().build();
                    }         
          }

          /**
           * PUT /api/pistas/{id}/liberar → cambiar a Disponible (ADMIN)
           * @param id identificador pista
           * @return pista editada (Pista) con id, nombre, estado
           */
          @PutMapping("/{id}/liberar")
          public ResponseEntity<?> liberar(@PathVariable Long id) {
                    try {
                              return ResponseEntity.ok(pistaService.liberar(id));
                    } catch (RuntimeException e) {
                              return ResponseEntity.notFound().build();
                    }
          }         
}