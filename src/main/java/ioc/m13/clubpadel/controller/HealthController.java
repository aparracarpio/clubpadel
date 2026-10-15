package ioc.m13.clubpadel.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 
 * HealthController
 * Controlador para verificar el estado de la aplicación. 
 * Lo utilizamos por que usamos Render y queremos que nos diga si la app está viva y funcionando.
 */

@RestController
public class HealthController {
    /* GET /api/health, verifica el estado de la aplicación */
    @GetMapping("/api/health")
    public Map<String, String> health() {
        return Map.of("status", "ok");
    }
}
