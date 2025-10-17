package br.com.haroldomorais.librarytest.model.usuario;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "usuarios")
@ToString
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Email(message = "Email deve ser no formato correto")
    @Column(name = "email")
    private String email;

    @Column(name = "matricula", unique = true , nullable = false)
    private String matricula;

}
