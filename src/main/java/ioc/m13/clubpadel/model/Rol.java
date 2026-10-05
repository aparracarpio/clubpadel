package ioc.m13.clubpadel.model;

import jakarta.persistence.*;

@Entity
@Table(name = "rol")
public class Rol {

          @Id
          @GeneratedValue(strategy = GenerationType.IDENTITY)
          private Long id;

          @Column(nullable = false, unique = true, length = 50)
          private String nombre;  // "ADMIN" o "USER"

          // Getters y setters
          public Long getId() { return id; }
          public void setId(Long id) { this.id = id; }

          public String getNombre() { return nombre; }
          public void setNombre(String nombre) { this.nombre = nombre; }
}