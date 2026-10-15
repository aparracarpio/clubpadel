package ioc.m13.clubpadel.dto;

/**
 * UsuarioResponse, DTO con datos publicos de un usuario.
 * No incluye password ni otros datos sensibles (hash).
 * Forma segura de exponer usuario en respuestas de la API.
 */
public class UsuarioResponse {
          // Atributos del usuario que se van a exponer en la respuesta
          private Long id;
          private String nombre;
          private String email;
          private String telefono;
          private String rol;

          // Constructor
          public UsuarioResponse(Long id, String nombre, String email, String telefono, String rol) {
                    this.id = id;
                    this.nombre = nombre;
                    this.email = email;
                    this.telefono = telefono;
                    this.rol = rol;
          }

          // Getters
          public Long getId() { return id; }
          public String getNombre() { return nombre; }
          public String getEmail() { return email; }
          public String getTelefono() { return telefono; }
          public String getRol() { return rol; }
}
