package br.com.haroldomorais.librarytest.repository;

import br.com.haroldomorais.librarytest.model.usuario.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByMatricula(String matricula);

    @Query("""
    SELECT u FROM Usuario u
    WHERE LOWER(u.nome) LIKE LOWER(CONCAT('%', :nome, '%'))
       OR LOWER(u.email) LIKE LOWER(CONCAT('%', :email, '%'))
       OR LOWER(u.matricula) LIKE LOWER(CONCAT('%', :matricula, '%'))
    """)
    Page<Usuario> buscarPorNomeEEmailMatricula(
            String nome,
            String email,
            String matricula,
            Pageable page);
}
