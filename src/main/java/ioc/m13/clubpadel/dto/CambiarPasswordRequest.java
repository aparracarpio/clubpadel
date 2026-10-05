package ioc.m13.clubpadel.dto;

/* Request para cambiar la contraseña 
* DTO para recibir datos de la petición POST /api/usuarios/password
* Email obtenido de token JWT
*/

public class CambiarPasswordRequest {
          private String passwordActual;
          private String passwordNuevo;

          // Getters y setters
          public String getPasswordActual() { return passwordActual; }
          public void setPasswordActual(String passwordActual) { this.passwordActual = passwordActual; }
          public String getPasswordNuevo() { return passwordNuevo; }
          public void setPasswordNuevo(String passwordNuevo) { this.passwordNuevo = passwordNuevo; }
}
