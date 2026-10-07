package ioc.m13.clubpadel.service;

import ioc.m13.clubpadel.dto.PistaRequest;
import ioc.m13.clubpadel.model.Pista;
import ioc.m13.clubpadel.repository.PistaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PistaService {

    @Autowired
    private PistaRepository pistaRepository;

    public List<Pista> listar() {
        return pistaRepository.findAll();
    }

    public List<Pista> listarPorEstado(String estado) {
        return pistaRepository.findByEstado(estado);
    }

    public Pista buscarPorId(Long id) {
        return pistaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pista no encontrada"));
    }

    public Pista crear(PistaRequest req) {
        Pista p = new Pista();
        p.setNombre(req.getNombre());
        p.setTipo(req.getTipo());
        p.setEstado("Disponible");
        return pistaRepository.save(p);
    }

    public Pista editar(Long id, PistaRequest req) {
        Pista p = buscarPorId(id);
        p.setNombre(req.getNombre());
        p.setTipo(req.getTipo());
        return pistaRepository.save(p);
    }

    public void eliminar(Long id) {
        if (!pistaRepository.existsById(id)) {
            throw new RuntimeException("Pista no encontrada");
        }
        pistaRepository.deleteById(id);
    }

    public Pista ocupar(Long id) {
        Pista p = buscarPorId(id);
        p.setEstado("Ocupada");
        return pistaRepository.save(p);
    }

    public Pista liberar(Long id) {
        Pista p = buscarPorId(id);
        p.setEstado("Disponible");
        return pistaRepository.save(p);
    }
}