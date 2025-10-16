package br.com.haroldomorais.librarytest.exception.dto;

import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ErroResponse {
    private int status;
    private String error;
    private String message;
}
