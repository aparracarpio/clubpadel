package ioc.m13.clubpadel.service;

import ioc.m13.clubpadel.dto.PistaRequest;
import ioc.m13.clubpadel.model.Pista;
import ioc.m13.clubpadel.repository.PistaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
/**
 * PistaService, servicio con la logica de pista.
 * Se encarga del CRUD basico y las transiciones de estado de pista 
 */
@Service
public class PistaService {
    // Repositorio JPA de pistas
    @Autowired
    private PistaRepository pistaRepository;
    /**
     * Ver todas las pistas del club 
     * @return devuelve lista de pistas
     */
    public List<Pista> listar() {
        return pistaRepository.findAll();
    }
    /**
     * Lista pistas filtradas por estado
     * @param estado Disponible o Ocupada
     * @return lista de pistas con el estado especificado
     */
    public List<Pista> listarPorEstado(String estado) {
        return pistaRepository.findByEstado(estado);
    }
    /**
     * Buscar pista por ID
     * @param id identificador pista
     * @return devuelve entidad Pista
     * @throws RuntimeException sino existe pista
     */
    public Pista buscarPorId(Long id) {
        return pistaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pista no encontrada"));
    }
    /**
     * Crear pista nueva, estado inicial siempre como Disponible.
     * @param req DTO co nombre y tipo de pista
     * @return pista guardada (con identificador unico)
     */
    public Pista crear(PistaRequest req) {
        Pista p = new Pista();
        p.setNombre(req.getNombre());
        p.setTipo(req.getTipo());
        p.setEstado("Disponible");
        return pistaRepository.save(p);
    }
    /**
     * Editar pista existente. Solo cambio de nombre y tipo.
     * @param id identificador pista a editar
     * @param req nuevos datos
     * @return pista actualizada
     * @throws RuntimeException si la pista no existe
     */
    public Pista editar(Long id, PistaRequest req) {
        Pista p = buscarPorId(id);
        p.setNombre(req.getNombre());
        p.setTipo(req.getTipo());
        return pistaRepository.save(p);
    }
    /**
     * Eliminar pista por id. Comprobamos que exita antes de eliminar la pista.
     * @param id identificador pista
     * @throws RuntimeException sino existe pista indicada
     */
    public void eliminar(Long id) {
        if (!pistaRepository.existsById(id)) {
            throw new RuntimeException("Pista no encontrada");
        }
        pistaRepository.deleteById(id);
    }
    /**
     * Cambia estado de pista a Ocupada
     * @param id identificador pista
     * @return devuelve pista actualizada
     */
    public Pista ocupar(Long id) {
        Pista p = buscarPorId(id);
        p.setEstado("Ocupada");
        return pistaRepository.save(p);
    }
    /**
     * Cambia estado de pista a Disponible
     * @param id identificador pista
     * @return devuelve pista actualizada
     */
    public Pista liberar(Long id) {
        Pista p = buscarPorId(id);
        p.setEstado("Disponible");
        return pistaRepository.save(p);
    }
}