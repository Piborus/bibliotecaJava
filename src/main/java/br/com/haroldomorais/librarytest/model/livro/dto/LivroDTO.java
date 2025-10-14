package br.com.haroldomorais.librarytest.model.livro.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LivroDTO {

    private String titulo;
    private String isbn;
    private String autor;
    private Integer quantidade;



}
