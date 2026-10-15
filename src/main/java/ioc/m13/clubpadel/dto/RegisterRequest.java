package ioc.m13.clubpadel.dto;

/**
 * DTO con datos enviados por el cliente en la petición POST /api/auth/register
 * Backend hashea contraseña con BCrypt antes de guardarla en la base de datos.
 */
public class RegisterRequest {
          // Campos que recibimos en el body de la petición POST /register
          private String nombre;
          private String email;
          private String password;
          private String telefono;

          // Getters y setters
          public String getNombre() {return this.nombre;}
          public String getEmail() {return this.email;}
          public String getPassword() {return this.password;}
          public String getTelefono() {return this.telefono;}

          public void setNombre(String nombre) {this.nombre = nombre;}
          public void setEmail(String email) {this.email = email;}
          public void setPassword(String password) {this.password = password;}
          public void setTelefono(String telefono) {this.telefono = telefono;}
}
