package ioc.m13.clubpadel.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public class ReservaResponse {
          private Long id;
          private Long idUsuario;
          private String nombreUsuario;
          private Long idPista;
          private String nombrePista;
          private LocalDate fecha;
          private LocalTime horaInicio;
          private LocalTime horaFin;
          private String estado;

          // Constructor
          public ReservaResponse(Long id, Long idUsuario, String nombreUsuario, Long idPista, 
                    String nombrePista, LocalDate fecha, LocalTime horaInicio, LocalTime horaFin, String estado) {
                    this.id = id;
                    this.idUsuario = idUsuario;
                    this.nombreUsuario = nombreUsuario;
                    this.idPista = idPista;
                    this.nombrePista = nombrePista;
                    this.fecha = fecha;
                    this.horaInicio = horaInicio;
                    this.horaFin = horaFin;
                    this.estado = estado;
          }

          // Getters
          public Long getId() { return id; }
          public Long getIdUsuario() { return idUsuario; }
          public String getNombreUsuario() { return nombreUsuario; }
          public Long getIdPista() { return idPista; }
          public String getNombrePista() { return nombrePista; }
          public LocalDate getFecha() { return fecha; }
          public LocalTime getHoraInicio() { return horaInicio; }
          public LocalTime getHoraFin() { return horaFin; }
          public String getEstado() { return estado; }

}
