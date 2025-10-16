package br.com.haroldomorais.librarytest.model.emprestimo.dto;

import br.com.haroldomorais.librarytest.model.emprestimo.Emprestimo;
import br.com.haroldomorais.librarytest.model.livro.Livro;
import br.com.haroldomorais.librarytest.model.usuario.Usuario;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class EmprestimoResumoDTO {

    private Long id;
    private LocalDateTime dataDoEmprestimo;
    private LocalDateTime dataPrevistaDaDevolucao;
    private LocalDateTime dataDaDevolucao;
    private Usuario usuario;
    private Long livroId;
    private String titulo;
    private String autor;
    private String isbn;
    private Long quantidadeEmprestada;

    public EmprestimoResumoDTO(
            Long id,
            LocalDateTime dataDoEmprestimo,
            LocalDateTime dataPrevistaDaDevolucao,
            LocalDateTime dataDaDevolucao,
            Usuario usuario,
            Long livroId,
            String titulo,
            String autor,
            String isbn,
            Long quantidadeEmprestada) {
        this.id = id;
        this.dataDoEmprestimo = dataDoEmprestimo;
        this.dataPrevistaDaDevolucao = dataPrevistaDaDevolucao;
        this.dataDaDevolucao = dataDaDevolucao;
        this.usuario = usuario;
        this.livroId = livroId;
        this.titulo = titulo;
        this.autor = autor;
        this.isbn = isbn;
        this.quantidadeEmprestada = quantidadeEmprestada;
    }
}


