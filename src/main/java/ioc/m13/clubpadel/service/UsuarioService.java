package ioc.m13.clubpadel.service;

import ioc.m13.clubpadel.dto.CambiarPasswordRequest;
import ioc.m13.clubpadel.dto.UsuarioResponse;
import ioc.m13.clubpadel.model.Usuario;
import ioc.m13.clubpadel.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
/**
 * UsuarioService, servicio con logica de Usuario.
 * Implementa CRUD basico, cambio de contraseña, mapeo entidad -> DTO (para no exponer hash en API)
 */

@Service
public class UsuarioService {
    /*Repositorio JPA de usuarios */
    @Autowired
    private UsuarioRepository usuarioRepository;
    /*Encoder BCrypt */
    @Autowired
    private PasswordEncoder passwordEncoder;
    /**
     * Lista todos los usuarios como DTOs (sin contraseña)
     * @return lista UsuarioResponse
     */
    @Transactional(readOnly = true)
    public List<UsuarioResponse> listar() {
        return usuarioRepository.findAll().stream().map(this::toResponse).toList();
    }
    /**
     * Buscar usuario por email y devolver como DTO
     * @param email correo del usuario
     * @return DTO correspondiente al usuario
     * @throws RuntimeException sino existe usuario
     */
    @Transactional(readOnly = true)
    public UsuarioResponse buscarPorEmail(String email) {
        Usuario u = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return toResponse(u);
    }
    /**
     * Cambiar contraseña usuario autenticado
     * @param email correo del usuario
     * @param req DTO con contraseña actual y nueva
     * @throws RuntimeException si el usuario no existe o la contraseña actual no coincide
     */
    public void cambiarPassword(String email, CambiarPasswordRequest req) {
        // Buscar usuario
        Usuario u = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        // Comprobar contraseñas
        if (!passwordEncoder.matches(req.getPasswordActual(), u.getPassword())) {
            throw new RuntimeException("Contraseña actual incorrecta");
        }
        // Guardar nuevas credenciales
        u.setPassword(passwordEncoder.encode(req.getPasswordNuevo()));
        usuarioRepository.save(u);
    }
    /**
     * Eliminar usuario mediante id
     * @param id identificador usuario
     * @throws RuntimeException si usuario no se encuentra
     */
    public void eliminar(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new RuntimeException("Usuario no encontrado");
        }
        usuarioRepository.deleteById(id);
    }
    /**
     * Convierte entidad Usuario en DTO seguro (sin contraseña)
     * @param u entidad Usuario
     * @return conversion DTO
     */
    private UsuarioResponse toResponse(Usuario u) {
        return new UsuarioResponse(
                u.getId(),
                u.getNombre(),
                u.getEmail(),
                u.getTelefono(),
                u.getRol().getNombre()
        );
    }
}