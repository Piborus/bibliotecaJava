package br.com.haroldomorais.librarytest.repository;

import br.com.haroldomorais.librarytest.model.livro.Livro;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LivroRepository extends JpaRepository<Livro, Long> {

    Optional<Livro> findByIsbn(String isbn);

    @Query("SELECT l FROM Livro l WHERE l.quantidade > 0")
    Page<Livro> buscarLivrosDisponiveis(Pageable pageable);
}
