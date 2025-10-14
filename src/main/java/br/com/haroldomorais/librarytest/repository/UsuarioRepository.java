package br.com.haroldomorais.librarytest.repository;

import br.com.haroldomorais.librarytest.model.usuario.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {


    @Query("SELECT u FROM Usuario u WHERE u.nome LIKE %:nome% OR u.email LIKE %:email% OR u.matricula LIKE %:matricula%")
    Page<Usuario> buscarPorNomeEEmailMatricula(String nome, String email, String matricula, Pageable page);
}
