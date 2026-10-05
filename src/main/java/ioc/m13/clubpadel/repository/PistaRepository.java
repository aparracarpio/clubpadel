package ioc.m13.clubpadel.repository;

import ioc.m13.clubpadel.model.Pista;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/*
 * @Repository es una anotación de Spring que marca esta interfaz como
 *   un bean de acceso a datos. Spring la detecta al escanear el paquete.
 *
 * JpaRepository<Pista, Long> te da GRATIS decenas de métodos CRUD:
 *   - findAll() → devuelve todas las pistas.
 *   - findById(id) → busca una pista por id.
 *   - save(pista) → inserta o actualiza.
 *   - deleteById(id) → borra por id.
 *   - count() → cuenta filas.
 *   - existsById(id) → comprueba si existe.
 *
 * El primer genérico es la entidad (Pista).
 * El segundo es el tipo de la clave primaria (Long).
 *
 * No implementar nada. Spring Data JPA genera la implementación
 * en tiempo de ejecución automáticamente.
 */
@Repository
public interface PistaRepository extends JpaRepository<Pista, Long> {

          List<Pista> findByEstado(String string);
}