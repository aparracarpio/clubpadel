package ioc.m13.clubpadel.model;

import jakarta.persistence.*;

/*
 * @Entity indica a JPA que esta clase está mapeada a una tabla.
 * @Table(name = "pista") especifica el nombre exacto de la tabla en la BD.
 *   Si no lo pones, JPA usa el nombre de la clase (Pista → pista).
 */
@Entity
@Table(name = "pista")
public class Pista {

          /*
          * @Id → esta es la clave primaria de la tabla.
          * @GeneratedValue(strategy = GenerationType.IDENTITY) →
          *   la BD genera el valor automáticamente (BIGSERIAL en PostgreSQL).
          *   Java no necesita asignarlo manualmente.
          */
          @Id
          @GeneratedValue(strategy = GenerationType.IDENTITY)
          private Long id;

          // Cada campo se mapea a una columna con el mismo nombre.
          // Puedes usar @Column(name="...") si el nombre difiere.
          private String nombre;
          private String tipo;
          private String estado;

          // Getters y setters: JPA los necesita para leer/escribir los valores.
          public Long getId() { return id; }
          public void setId(Long id) { this.id = id; }

          public String getNombre() { return nombre; }
          public void setNombre(String nombre) { this.nombre = nombre; }

          public String getTipo() { return tipo; }
          public void setTipo(String tipo) { this.tipo = tipo; }

          public String getEstado() { return estado; }
          public void setEstado(String estado) { this.estado = estado; }
}