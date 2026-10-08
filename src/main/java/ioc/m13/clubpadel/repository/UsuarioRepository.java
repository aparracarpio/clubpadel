package ioc.m13.clubpadel.repository;

import ioc.m13.clubpadel.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // 
    @Query("SELECT u FROM Usuario u JOIN FETCH u.rol WHERE u.email = :email")
    Optional<Usuario> findByEmailWithRol(@Param("email") String email);
    
    // SELECT * FROM usuario WHERE email = ?
    Optional<Usuario> findByEmail(String email);

    // SELECT COUNT(*) > 0 FROM usuario WHERE email = ?
    boolean existsByEmail(String email);
}