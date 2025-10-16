package br.com.haroldomorais.librarytest.model.livro;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "livros")
public class Livro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "titulo", nullable = false)
    private String titulo;

    @NotBlank
    @Column(name = "autor", nullable = false)
    private String autor;

    @Column(name = "isbn", nullable = false, unique = true)
    private String isbn;

    @Column(name = "quantidade")
    private Integer quantidade;

}
