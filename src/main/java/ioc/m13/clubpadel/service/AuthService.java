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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private RolRepository rolRepository;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private JwtUtil jwtUtil;

    public Usuario register(RegisterRequest req) {
        if (usuarioRepository.existsByEmail(req.getEmail())) {
            throw new RuntimeException("El email ya está registrado");
        }
        Rol rolUser = rolRepository.findByNombre("USER")
                .orElseThrow(() -> new RuntimeException("Rol USER no encontrado"));
        Usuario u = new Usuario();
        u.setNombre(req.getNombre());
        u.setEmail(req.getEmail());
        u.setPassword(passwordEncoder.encode(req.getPassword()));
        u.setTelefono(req.getTelefono());
        u.setRol(rolUser);
        return usuarioRepository.save(u);
    }

    public LoginResponse login(LoginRequest req) {
        // 1. Buscar el usuario
        Usuario usuario = usuarioRepository.findByEmailWithRol(req.getEmail())
                .orElseThrow(() -> new RuntimeException("Credenciales incorrectas (mail no encontrado)"));

        // 2. Comparar la contraseña con el hash BCrypt
        //if (!passwordEncoder.matches(req.getPassword(), usuario.getPassword())) {
        //    throw new RuntimeException("Credenciales incorrectas (contraseña incorrecta)");
        //}

        // 3. Generar el token
        String token = jwtUtil.generateToken(usuario.getEmail(), usuario.getRol().getNombre());
        return new LoginResponse(token, usuario.getRol().getNombre(), usuario.getNombre());
    }
}
