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

/**
 * AuthService, servicio de autenticación y registro de usuarios.
 * Proporciona métodos para registrar nuevos usuarios y autenticar usuarios existentes.
 */
@Service
public class AuthService {
    /* Servicio de autenticación y registro de usuarios */
    // Repositorio JPA de usuarios
    @Autowired private UsuarioRepository usuarioRepository;
    // Repositorio JPA de roles
    @Autowired private RolRepository rolRepository;
    // Codificador de contraseñas (BCrypt)
    @Autowired private PasswordEncoder passwordEncoder;
    // Utilidad para generar y validar tokens JWT
    @Autowired private JwtUtil jwtUtil;
    
    /**
     * Registra un nuevo usuario en la base de datos.
     * Si el email ya está registrado, lanza una excepción.
     * Asigna el rol "USER" al nuevo usuario y guarda la contraseña codificada.
     * @param req DTO con los datos del usuario a registrar (nombre, email, password, teléfono)
     * @return El usuario registrado (entidad Usuario)
     * @throws RuntimeException si el email ya está registrado o el rol "USER" no se encuentra en la base de datos.
     */
    public Usuario register(RegisterRequest req) {
        // Comprobar si email ya esta registrado
        if (usuarioRepository.existsByEmail(req.getEmail())) {
            throw new RuntimeException("El email ya está registrado");
        }
        // Recuperar rol por defecto "USER" de la base de datos
        Rol rolUser = rolRepository.findByNombre("USER")
                .orElseThrow(() -> new RuntimeException("Rol USER no encontrado"));

        // Construir nuevo usuario y guardarlo en la BD        
        Usuario u = new Usuario();

        u.setNombre(req.getNombre());
        u.setEmail(req.getEmail());
        u.setPassword(passwordEncoder.encode(req.getPassword()));
        u.setTelefono(req.getTelefono());
        u.setRol(rolUser);
        // Guardar usuario en la base de datos y devolverlo
        return usuarioRepository.save(u);
    }

    /**
     * Autentica un usuario con email y password.
     * Si las credenciales son correctas, genera un token JWT y lo devuelve.
     * @param req DTO con email y contraseña
     * @return LoginResponse con token, rol y nombre
     * @throws RuntimeException si el email no existe o la contraseña falla
     */
    public LoginResponse login(LoginRequest req) {
        // Buscar el usuario
        Usuario usuario = usuarioRepository.findByEmailWithRol(req.getEmail())
                .orElseThrow(() -> new RuntimeException("Credenciales incorrectas (mail no encontrado)"));

        // Comparar la contraseña con el hash BCrypt
        if (!passwordEncoder.matches(req.getPassword(), usuario.getPassword())) {
           throw new RuntimeException("Credenciales incorrectas (contraseña incorrecta)");
        }

        // Generar el token
        String token = jwtUtil.generateToken(usuario.getEmail(), usuario.getRol().getNombre());
        
        // Devolver DTO de respuesta
        return new LoginResponse(token, usuario.getRol().getNombre(), usuario.getNombre());
    }
}
