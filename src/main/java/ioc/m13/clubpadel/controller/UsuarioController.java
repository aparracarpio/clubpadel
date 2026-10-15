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

/**
 * 
 * UsuarioController, controlador REST para recurso Usuario.
 *        Ruta base: /api/usuarios
 *        Metodos: GET, PUT, DELETE
 *                  GET → listar todos (ADMIN)
 *                  PUT → actualizar perfil (USER/ADMIN)
 *                  DELETE → eliminar (ADMIN)
 */

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {
          /* Servicio con logica de negocio para usuarios */
          @Autowired
          private UsuarioService usuarioService;

          /**
           * GET /api/usuarios → devuelve lista de todos los usuarios (solo ADMIN)
           * @return lista usuarios (UsuarioResponse) con email, nombre y rol
           */
          @GetMapping
          public List<UsuarioResponse> listar() {
                    return usuarioService.listar();
          }

          /**
           * GET /api/usuarios/me → devuelve datos del usuario logueado (email, nombre, rol)
           * @param auth información del usuario logueado (email)
           * @return UsuarioResponse con datos del usuario logueado
           */
          @GetMapping("/me")
          public UsuarioResponse me(Authentication auth) {
                    return usuarioService.buscarPorEmail(auth.getName());
          }

          /**
           * PUT /api/usuarios/password → cambiar contraseña del usuario logueado
           * @param req DTO con password actual y nueva contraseña
           * @param auth información del usuario logueado (email)
           * @return 200 -> contraseña actualizada correctamente
           *         400 -> error (password actual incorrecta, nueva contraseña inválida, etc.)
           */
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

          /**
           * DELETE /api/usuarios/{id} -> eliminar usuario por id (solo ADMIN)
           * @param id identificador usuario
           * @return 200 -> usuario eliminado correctamente
           *         400 -> error (usuario no existe, etc.)
           */
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