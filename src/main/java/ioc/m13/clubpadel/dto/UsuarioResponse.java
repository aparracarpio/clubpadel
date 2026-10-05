package ioc.m13.clubpadel.dto;

public class UsuarioResponse {
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
