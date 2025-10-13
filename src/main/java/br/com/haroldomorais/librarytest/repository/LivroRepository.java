package br.com.haroldomorais.librarytest.repository;

import br.com.haroldomorais.librarytest.model.livro.Livro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LivroRepository extends JpaRepository<Livro, Long> {
}
