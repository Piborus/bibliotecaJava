package br.com.haroldomorais.librarytest.repository;

import br.com.haroldomorais.librarytest.model.emprestimo.Emprestimo;
import br.com.haroldomorais.librarytest.model.livro.Livro;
import br.com.haroldomorais.librarytest.model.usuario.Usuario;
import br.com.haroldomorais.librarytest.model.emprestimo.dto.EmprestimoResumoDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmprestimoRepository extends JpaRepository<Emprestimo, Long> {

    @Query("SELECT COUNT(e) FROM Emprestimo e WHERE e.usuario = :usuario AND e.dataDaDevolucao IS NULL")
    long contarEmprestimosAtivosPorUsuario(Usuario usuario);

    @Query("SELECT e FROM Emprestimo e WHERE e.usuario = :usuario AND e.livro = :livro AND e.dataDaDevolucao IS NULL")
    Optional<List<Emprestimo>> findByUsuarioAndLivroAndDataDaDevolucaoIsNull(Usuario usuario, Livro livro);

    @Query("SELECT new br.com.haroldomorais.librarytest.model.emprestimo.dto.EmprestimoResumoDTO(" +
            "e.id, e.dataDoEmprestimo, e.dataPrevistaDaDevolucao, e.dataDaDevolucao, e.usuario, " +
            "e.livro.id, e.livro.titulo, e.livro.autor, e.livro.isbn, COUNT(e)) " +
            "FROM Emprestimo e WHERE e.usuario.id = :usuarioId AND e.dataDaDevolucao IS NULL " +
            "GROUP BY e.id, e.dataDoEmprestimo, e.dataPrevistaDaDevolucao, e.dataDaDevolucao, e.usuario, " +
            "e.livro.id, e.livro.titulo, e.livro.autor, e.livro.isbn")
    List<EmprestimoResumoDTO> buscarResumoEmprestimosPorUsuario(Long usuarioId);

    @Query("SELECT new br.com.haroldomorais.librarytest.model.emprestimo.dto.EmprestimoResumoDTO(" +
            "e.id, e.dataDoEmprestimo, e.dataPrevistaDaDevolucao, e.dataDaDevolucao, e.usuario, " +
            "e.livro.id, e.livro.titulo, e.livro.autor, e.livro.isbn, COUNT(e)) " +
            "FROM Emprestimo e WHERE e.usuario.id = :usuarioId AND e.dataDaDevolucao IS NULL " +
            "AND e.dataPrevistaDaDevolucao < CURRENT_TIMESTAMP " +
            "GROUP BY e.id, e.dataDoEmprestimo, e.dataPrevistaDaDevolucao, e.dataDaDevolucao, e.usuario, " +
            "e.livro.id, e.livro.titulo, e.livro.autor, e.livro.isbn")
    List<EmprestimoResumoDTO> buscarResumoEmprestimosEmAtrasoPorUsuario(Long usuarioId);
}
