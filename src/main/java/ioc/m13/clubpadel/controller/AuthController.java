package ioc.m13.clubpadel.controller;

import ioc.m13.clubpadel.dto.LoginRequest;
import ioc.m13.clubpadel.dto.LoginResponse;
import ioc.m13.clubpadel.dto.RegisterRequest;
import ioc.m13.clubpadel.model.Usuario;
import ioc.m13.clubpadel.service.AuthService;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;

import java.util.Map;

/**
 * 
 * AuthController, controlador de autenticación y registro de usuarios.
 * Expone endpoints para registro, login y debug de autenticación.
 * 
 */

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    // Servicio que implementa logica de login/registro de usuarios
    @Autowired
    private AuthService authService;
    // Repositorio para acceder a datos de usuarios
    @Autowired private ioc.m13.clubpadel.repository.UsuarioRepository usuarioRepository;
    // PasswordEncoder para verificar contraseñas en debug
    @Autowired private org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;
    // Logger para dejar registros de errores y eventos importantes en el log del servidor
    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    
    /**
     * POST /api/auth/register, registra un nuevo usuario con datos de RegisterRequest (email, password, nombre, rol). 
     * @param req datos de registro (email, password, nombre, rol)
     * @return 200 -> usuario registrado correctamente 
     *         400 -> error de validación (email ya existe, rol inválido, etc.)
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest req) {
        try {
            Usuario u = authService.register(req);
            return ResponseEntity.ok(Map.of(
                    "mensaje", "Usuario registrado correctamente",
                    "id", u.getId(),
                    "email", u.getEmail()
            ));
        } catch (RuntimeException e) {
            // Si hay error de validación (email ya existe, rol inválido, etc.) devolvemos 400 con mensaje de error
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * POST /api/auth/login, verifica credenciales y devuelve un token JWT junto con los datos del usuario 
     *      (ROL y nombre usuario) si son correctas
     * @param req DTO con email y password del usuario
     * @return 200 -> LoginResponse con token JWT y datos del usuario
     *         401 -> credenciales incorrectas
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
        try {
            LoginResponse res = authService.login(req);
            return ResponseEntity.ok(res);
        } catch (Exception e) {
            log.error("ERROR EN LOGIN para {}: {}", req.getEmail(), e.getMessage(), e);
            return ResponseEntity.status(401).body(Map.of("error", "Credenciales incorrectas"));
        }
    }

    /**
     * POST /api/auth/debug/check, verifica si un usuario existe y si la contraseña es correcta (solo para debug)
     * @param req DTO con email y password del usuario
     * @return Map con información de verificación
     */
    @PostMapping("/debug/check")
    public Map<String, Object> debugCheck(@RequestBody LoginRequest req) {
        var usuarioOpt = usuarioRepository.findByEmail(req.getEmail());
        if (usuarioOpt.isEmpty()) {
            return Map.of("existe", false, "mensaje", "Usuario no encontrado");
        }
        var u = usuarioOpt.get();
        boolean matches = passwordEncoder.matches(req.getPassword(), u.getPassword());
        return Map.of(
            "existe", true,
            "email", u.getEmail(),
            "hashGuardado", u.getPassword(),
            "passwordEnviada", req.getPassword(),
            "coincide", matches
        );
    }
}
