package br.com.haroldomorais.librarytest.controller;

import br.com.haroldomorais.librarytest.model.livro.Livro;
import br.com.haroldomorais.librarytest.model.livro.dto.LivroDTO;
import br.com.haroldomorais.librarytest.service.LivroService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/livros")
@RequiredArgsConstructor
public class LivroController {

    private final LivroService service;

    @PostMapping
    public ResponseEntity<Void> cadastrarLivro(@RequestBody LivroDTO livroDto) {
        service.cadastrarLivro(livroDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    public ResponseEntity<Livro> buscarPorLivroId(@PathVariable Long id){
        service.buscarPorId(id);
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PatchMapping("{id}")
    public ResponseEntity<Livro> atualizarLivroPorId(@PathVariable Long id, @RequestBody LivroDTO livroDto){
        Livro livroAtualizado = service.atualizarLivro(id, livroDto);
        return ResponseEntity.ok(livroAtualizado);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deletarLivroPorId(@PathVariable Long id) {
        service.deletarLivroPorId(id);
        return ResponseEntity.noContent().build();
    }
}
