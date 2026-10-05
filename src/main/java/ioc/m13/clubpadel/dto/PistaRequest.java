package ioc.m13.clubpadel.dto;

/**
 * DTO para recibir datos de una pista en POST /api/pistas
 *        Y PUT /api/pistas/{id}
 */
public class PistaRequest {
          // Campos que recibimos en el body de la petición POST /pistas
          private String nombre;
          private String ubicacion;
          private String tipo;

          // Getters y setters
          public String getNombre() { return nombre; }
          public void setNombre(String nombre) { this.nombre = nombre; }

          public String getUbicacion() { return ubicacion; }
          public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }

          public String getTipo() { return tipo; }
          public void setTipo(String tipo) { this.tipo = tipo; }
}
