package br.com.haroldomorais.librarytest.model.livro.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LivroDTO {

    @NotBlank
    private String titulo;

    @NotBlank
    private String autor;

    @NotBlank
    private String isbn;

    @Min(0)
    private Integer quantidade;

}
