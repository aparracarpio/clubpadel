package ioc.m13.clubpadel.dto;

public class LoginResponse {
          // Campos que enviamos en la respuesta de la petición POST /login
          private String token;
          private String rol;
          private String nombre;

          // Constructor
          public LoginResponse(String token, String rol, String nombre) {
                    this.token = token;
                    this.rol = rol;
                    this.nombre = nombre;
          }

          // Getters y setters
          public String getToken() { return token; }
          public void setToken(String token) { this.token = token; }
          
          public String getRol() { return rol; }
          public void setRol(String rol) { this.rol = rol; }

          public String getNombre() { return nombre; }
          public void setNombre(String nombre) { this.nombre = nombre; }
}
