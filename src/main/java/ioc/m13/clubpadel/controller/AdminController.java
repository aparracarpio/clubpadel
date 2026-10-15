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
/**
 * 
 * AdminController: controlador exclusivo de administrador.
 * Solo llegan aquí peticiones de usuarios con rol ADMIN, gracias a la configuración de seguridad.
 * 
 * 
 */

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    // Servicios con logica de negocio para usuarios
    @Autowired private UsuarioService usuarioService;
    // Servicio con logica de negocio para reservas
    @Autowired private ReservaService reservaService;
    // Repositorio (JPA) para acceder a datos de usuarios
    @Autowired private UsuarioRepository usuarioRepository;

    /**
     * GET /api/admin/verificar/{email} → devuelve datos usuario junto a la lista de sus reservas.
     * Solo accesible para usuarios con rol ADMIN para inspeccionar datos de otros usuarios.
     * @param email correo usuario a consultar
     * @return ResponseEntity con datos del usuario y sus reservas, o 404 si no existe.
     */
    @GetMapping("/verificar/{email}")
    public ResponseEntity<?> verificar(@PathVariable String email) {
        try {
            // Buscar usuario por correo
            UsuarioResponse usuario = usuarioService.buscarPorEmail(email);
            // Obtener lista de reservas del usuario
            var reservas = usuarioRepository.findByEmail(email)
                    .map(u -> reservaService.misReservas(u.getEmail()))
                    .orElse(List.of());
            // Devuelve datos en mapa JSON: { "usuario": {...}, "reservas": [...] }
            return ResponseEntity.ok(Map.of(
                    "usuario", usuario,
                    "reservas", reservas
            ));
        } catch (RuntimeException e) {
            // Si no existe usuario lanza excepción, devolvemos 404
            return ResponseEntity.notFound().build();
        }
    }
}