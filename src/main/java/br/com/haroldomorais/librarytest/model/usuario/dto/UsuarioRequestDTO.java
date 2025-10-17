package br.com.haroldomorais.librarytest.model.usuario.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioRequestDTO {

    @NotBlank
    private String nome;

    @Email(message = "Email deve ser no formato correto")
    private String email;
}
