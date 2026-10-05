package ioc.m13.clubpadel.dto;

import java.time.LocalDate;
import java.time.LocalTime;

/**
* DTO para recibir datos de una reserva en POST /api/reservas
*/
public class ReservaRequest {
          private Long idPista;
          private LocalDate fecha;
          private LocalTime horaInicio;
          private LocalTime horaFin;

          // Getters y setters
          public Long getIdPista() { return idPista; }
          public void setIdPista(Long idPista) { this.idPista = idPista; }
          public LocalDate getFecha() { return fecha; }
          public void setFecha(LocalDate fecha) { this.fecha = fecha; }
          public LocalTime getHoraInicio() { return horaInicio; }
          public void setHoraInicio(LocalTime horaInicio) { this.horaInicio = horaInicio; }
          public LocalTime getHoraFin() { return horaFin; }
          public void setHoraFin(LocalTime horaFin) { this.horaFin = horaFin; }
          
}
