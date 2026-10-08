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

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    
    @Autowired
    private AuthService authService;

    @Autowired private ioc.m13.clubpadel.repository.UsuarioRepository usuarioRepository;
    @Autowired private org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;
    
    // POST /api/auth/register
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
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    // POST /api/auth/login
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
