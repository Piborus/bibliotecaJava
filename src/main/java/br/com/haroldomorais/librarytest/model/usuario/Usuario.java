package br.com.haroldomorais.librarytest.model.usuario;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome")
    private String nome;

    @Email(message = "Email deve ser no formato correto")
    @Column(name = "email")
    private String email;

    @NotBlank
    @Column(name = "matricula", unique = true)
    private String matricula;

}
