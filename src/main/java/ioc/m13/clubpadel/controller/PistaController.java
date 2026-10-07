package ioc.m13.clubpadel.controller;

import ioc.m13.clubpadel.dto.PistaRequest;
import ioc.m13.clubpadel.model.Pista;
import ioc.m13.clubpadel.service.PistaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pistas")
public class PistaController {

          @Autowired
          private PistaService pistaService;

          // GET /api/pistas → todas (público)
          @GetMapping
          public List<Pista> getAll() {
                    return pistaService.listar();
          }

          // GET /api/pistas/disponibles → solo disponibles (público)
          @GetMapping("/disponibles")
          public List<Pista> getDisponibles() {
                    return pistaService.listarPorEstado("Disponible");
          }

          // GET /api/pistas/ocupadas → solo ocupadas (público)
          @GetMapping("/ocupadas")
          public List<Pista> getOcupadas() {
                    return pistaService.listarPorEstado("Ocupada");
          }

          // GET /api/pistas/{id} → una pista (público)
          @GetMapping("/{id}")
          public ResponseEntity<?> getById(@PathVariable Long id) {
                    try {
                              return ResponseEntity.ok(pistaService.buscarPorId(id));
                    } catch (RuntimeException e) {
                              return ResponseEntity.notFound().build();
                    }
          }

          // POST /api/pistas → crear (ADMIN)
          @PostMapping
          public Pista crear(@RequestBody PistaRequest req) {
                    return pistaService.crear(req);
          }

          // PUT /api/pistas/{id} → editar (ADMIN)
          @PutMapping("/{id}")
          public ResponseEntity<?> editar(@PathVariable Long id, @RequestBody PistaRequest req) {
                    try {
                              return ResponseEntity.ok(pistaService.editar(id, req));
                    } catch (RuntimeException e) {
                              return ResponseEntity.notFound().build();
                    }
          }

          // DELETE /api/pistas/{id} → eliminar (ADMIN)
          @DeleteMapping("/{id}")
          public ResponseEntity<?> eliminar(@PathVariable Long id) {
                    try {
                              pistaService.eliminar(id);
                              return ResponseEntity.ok(Map.of("mensaje", "Pista eliminada"));
                    } catch (RuntimeException e) {
                              return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
                    }
          }

          // PUT /api/pistas/{id}/ocupar → cambiar a Ocupada (ADMIN)
          @PutMapping("/{id}/ocupar")
          public ResponseEntity<?> ocupar(@PathVariable Long id) {
                    try {
                              return ResponseEntity.ok(pistaService.ocupar(id));
                    } catch (RuntimeException e) {
                              return ResponseEntity.notFound().build();
                    }         
          }

          // PUT /api/pistas/{id}/liberar → cambiar a Disponible (ADMIN)
          @PutMapping("/{id}/liberar")
          public ResponseEntity<?> liberar(@PathVariable Long id) {
                    try {
                              return ResponseEntity.ok(pistaService.liberar(id));
                    } catch (RuntimeException e) {
                              return ResponseEntity.notFound().build();
                    }
          }         
}