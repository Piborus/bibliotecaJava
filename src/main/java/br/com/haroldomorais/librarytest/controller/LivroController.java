package br.com.haroldomorais.librarytest.controller;

import br.com.haroldomorais.librarytest.model.livro.dto.LivroDTO;
import br.com.haroldomorais.librarytest.service.LivroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/livros")
public class LivroController {
}
