package br.com.haroldomorais.librarytest.model.livro;

import br.com.haroldomorais.librarytest.model.livro.dto.LivroDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "livros")
@Builder
public class Livro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "titulo", nullable = false)
    private String titulo;

    @Column(name = "isbn", nullable = false, unique = true)
    private String isbn;

    @NotBlank
    @Column(name = "autor", nullable = false)
    private String autor;

    @Min(0)
    @Column(name = "quantidade")
    private Integer quantidade;

}
