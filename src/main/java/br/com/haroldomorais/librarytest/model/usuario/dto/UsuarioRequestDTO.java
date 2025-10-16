package br.com.haroldomorais.librarytest.model.usuario.dto;

import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioRequestDTO {

    private String nome;

    @Email(message = "Email deve ser no formato correto")
    private String email;
}
