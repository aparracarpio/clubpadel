package ioc.m13.clubpadel.controller;

import ioc.m13.clubpadel.dto.CambiarPasswordRequest;
import ioc.m13.clubpadel.dto.UsuarioResponse;
import ioc.m13.clubpadel.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

          @Autowired
          private UsuarioService usuarioService;

          // GET /api/usuarios → listar todos (ADMIN)
          @GetMapping
          public List<UsuarioResponse> listar() {
                    return usuarioService.listar();
          }

          // GET /api/usuarios/me → mi perfil
          @GetMapping("/me")
          public UsuarioResponse me(Authentication auth) {
                    return usuarioService.buscarPorEmail(auth.getName());
          }

          // PUT /api/usuarios/password → cambiar contraseña
          @PutMapping("/password")
          public ResponseEntity<?> cambiarPassword(@RequestBody CambiarPasswordRequest req,
                                                  Authentication auth) {
                    try {
                              usuarioService.cambiarPassword(auth.getName(), req);
                              return ResponseEntity.ok(Map.of("mensaje", "Contraseña actualizada"));
                    } catch (RuntimeException e) {
                              return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
                    }
          }

          // DELETE /api/usuarios/{id} → eliminar (ADMIN)
          @DeleteMapping("/{id}")
          public ResponseEntity<?> eliminar(@PathVariable Long id) {
                    try {
                              usuarioService.eliminar(id);
                              return ResponseEntity.ok(Map.of("mensaje", "Usuario eliminado"));
                    } catch (RuntimeException e) {
                              return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
                    }
          }
}