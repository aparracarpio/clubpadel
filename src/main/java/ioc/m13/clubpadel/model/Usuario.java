package ioc.m13.clubpadel.model;

import jakarta.persistence.*;

@Entity
@Table(name = "usuario")
public class Usuario {

          @Id
          @GeneratedValue(strategy = GenerationType.IDENTITY)
          private Long id;

          @Column(nullable = false, length = 100)
          private String nombre;

          @Column(nullable = false, unique = true, length = 150)
          private String email;

          @Column(nullable = false, length = 255)
          private String password;  // Guardará el hash BCrypt, no texto plano

          @Column(length = 20)
          private String telefono;

          /*
          * @ManyToOne: muchos usuarios pueden tener el mismo rol.
          * @JoinColumn: columna en la tabla usuario que guarda la FK.
          *   Aquí se llamará id_rol, igual que en tu script SQL.
          * fetch = LAZY: no carga el rol hasta que lo pidas con getRol().
          *   Es la opción recomendada para rendimiento.
          */
          @ManyToOne(fetch = FetchType.LAZY)
          @JoinColumn(name = "id_rol", nullable = false)
          private Rol rol;

          // Getters y setters
          public Long getId() { return id; }
          public void setId(Long id) { this.id = id; }

          public String getNombre() { return nombre; }
          public void setNombre(String nombre) { this.nombre = nombre; }

          public String getEmail() { return email; }
          public void setEmail(String email) { this.email = email; }

          public String getPassword() { return password; }
          public void setPassword(String password) { this.password = password; }

          public String getTelefono() { return telefono; }
          public void setTelefono(String telefono) { this.telefono = telefono; }

          public Rol getRol() { return rol; }
          public void setRol(Rol rol) { this.rol = rol; }
}