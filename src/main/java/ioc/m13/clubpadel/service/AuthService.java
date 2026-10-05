package ioc.m13.clubpadel.service;

import ioc.m13.clubpadel.dto.LoginRequest;
import ioc.m13.clubpadel.dto.LoginResponse;
import ioc.m13.clubpadel.dto.RegisterRequest;
import ioc.m13.clubpadel.model.Rol;
import ioc.m13.clubpadel.model.Usuario;
import ioc.m13.clubpadel.repository.RolRepository;
import ioc.m13.clubpadel.repository.UsuarioRepository;
import ioc.m13.clubpadel.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    // Registro de un nuevo usuario (rol USER por defecto)
    public Usuario register(RegisterRequest req) {
        // Comprobar si ya existe
        if (usuarioRepository.existsByEmail(req.getEmail())) {
            throw new RuntimeException("El email ya está registrado");
        }

        // Buscar el rol USER
        Rol rolUser = rolRepository.findByNombre("USER")
                .orElseThrow(() -> new RuntimeException("Rol USER no encontrado en la BD"));

        Usuario u = new Usuario();
        u.setNombre(req.getNombre());
        u.setEmail(req.getEmail());
        u.setPassword(passwordEncoder.encode(req.getPassword()));  // BCrypt
        u.setTelefono(req.getTelefono());
        u.setRol(rolUser);

        return usuarioRepository.save(u);
    }

    // Login: autentica y devuelve JWT + datos del usuario
    public LoginResponse login(LoginRequest req) {
        // Autentica con Spring Security (compara password con el hash BCrypt)
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword())
        );

        // Si llega aquí, las credenciales son correctas
        Usuario usuario = usuarioRepository.findByEmail(req.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Generar token con email y rol
        String token = jwtUtil.generateToken(usuario.getEmail(), usuario.getRol().getNombre());

        return new LoginResponse(token, usuario.getRol().getNombre(), usuario.getNombre());
    }
}