package ioc.m13.clubpadel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/*
 * @SpringBootApplication es una anotación compuesta que incluye:
 *   - @Configuration: indica que esta clase puede definir beans.
 *   - @EnableAutoConfiguration: activa la configuración automática de Spring Boot
 *     (detecta dependencias y configura Tomcat, JPA, Security, etc. solos).
 *   - @ComponentScan: escanea el paquete actual y subpaquetes buscando
 *     @Component, @Service, @Repository, @Controller, @RestController, etc.
 *
 * 
 */
@SpringBootApplication
public class ClubpadelApplication {

	public static void main(String[] args) {
		// Arranca el contexto de Spring, levanta Tomcat y deja la app escuchando en el puerto 8080.
		SpringApplication.run(ClubpadelApplication.class, args);
	}

}