package ioc.m13.clubpadel.dto;

/**
 * DTO para recibir datos de una pista en POST /api/pistas
 *        Y PUT /api/pistas/{id}
 * El admin es el unico que puede crear o modificar pistas, 
 * por eso no se envia el id del admin en el body, se obtiene del token JWT.
 */
public class PistaRequest {
          // Campos que recibimos en el body de la petición POST /pistas
          private String nombre;
          private String tipo;

          // Getters y setters
          public String getNombre() { return nombre; }
          public void setNombre(String nombre) { this.nombre = nombre; }

          public String getTipo() { return tipo; }
          public void setTipo(String tipo) { this.tipo = tipo; }
}
