package ioc.m13.clubpadel.controller;

import ioc.m13.clubpadel.model.Pista;
import ioc.m13.clubpadel.repository.PistaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pistas")
public class PistaController {

          /* Repositorio de pistas */      
          @Autowired
          private PistaRepository pistaRepository;

          /*Devuelve una lista de todas las pistas en formato JSON */
          @GetMapping
          public List<Pista> getAll() {
                    return pistaRepository.findAll();
          }

          /*Devuelve una lista de pistas por estado disponibles en formato JSON */        

          @GetMapping("/disponibles")
          public List<Pista> getPistasByEstado() {
                    return pistaRepository.findByEstado("Disponible");
          }

          /*Devuelve una lista de pistas por estado ocupadas en formato JSON */

          @GetMapping("/ocupadas")
          public List<Pista> getPistasOcupadas() {
                    return pistaRepository.findByEstado("Ocupada");
          }
}