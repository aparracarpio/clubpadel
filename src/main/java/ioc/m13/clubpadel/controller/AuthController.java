package ioc.m13.clubpadel.controller;

import ioc.m13.clubpadel.dto.LoginRequest;
import ioc.m13.clubpadel.dto.LoginResponse;
import ioc.m13.clubpadel.dto.RegisterRequest;
import ioc.m13.clubpadel.model.Usuario;
import ioc.m13.clubpadel.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

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

    // POST /api/auth/login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
        try {
            LoginResponse res = authService.login(req);
            return ResponseEntity.ok(res);
        } catch (Exception e) {
            return ResponseEntity.status(401).body(Map.of("error", "Credenciales incorrectas"));
        }
    }
}