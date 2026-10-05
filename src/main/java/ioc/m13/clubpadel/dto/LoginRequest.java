package ioc.m13.clubpadel.dto;

public class LoginRequest {
          // Campos que recibimos en el body de la petición POST /login 
          private String email;
          private String password;

          // Getters y setters

          public String getEmail() { return email; }
          public void setEmail(String email) { this.email = email; }
          public String getPassword() { return password; }
          public void setPassword(String password) { this.password = password; }

}
