package br.com.haroldomorais.librarytest.controller;

import br.com.haroldomorais.librarytest.model.livro.Livro;
import br.com.haroldomorais.librarytest.model.livro.dto.LivroRequestDTO;
import br.com.haroldomorais.librarytest.model.livro.dto.LivroResponseDTO;
import br.com.haroldomorais.librarytest.service.LivroService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/livros")
@RequiredArgsConstructor
public class LivroController {

    private final LivroService service;

    @Operation(summary = "Cadastrar Livro")
    @PostMapping
    public ResponseEntity<Void> cadastrarLivro(@RequestBody @Valid LivroRequestDTO livroDto) {
        service.cadastrarLivro(livroDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(summary = "Buscar Livro por Id")
    @GetMapping("{id}")
    public ResponseEntity<LivroResponseDTO> buscarPorLivroId(@PathVariable Long id){
        Livro livro = service.buscarPorId(id);
        LivroResponseDTO dto = new LivroResponseDTO(livro.getId(), livro.getTitulo(), livro.getAutor(), livro.getIsbn(), livro.getQuantidade());
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Buscar Livro por ISBN")
    @GetMapping("/isbn/{isbn}")
    public ResponseEntity<LivroResponseDTO> buscarPorIsbn(@PathVariable String isbn){
        Livro livro = service.buscarPorIsbn(isbn);
        LivroResponseDTO dto = new LivroResponseDTO(livro.getId(), livro.getTitulo(), livro.getAutor(), livro.getIsbn(), livro.getQuantidade());
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Atualizar Livro por Id")
    @PatchMapping("{id}")
    public ResponseEntity<LivroResponseDTO> atualizarLivroPorId(@PathVariable Long id, @RequestBody @Valid LivroRequestDTO livroDto){
        Livro livroAtualizado = service.atualizarLivro(id, livroDto);
        LivroResponseDTO dto = new LivroResponseDTO(livroAtualizado.getId(), livroAtualizado.getTitulo(), livroAtualizado.getAutor(), livroAtualizado.getIsbn(), livroAtualizado.getQuantidade());
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Deletar Livro por Id")
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deletarLivroPorId(@PathVariable Long id) {
        service.deletarLivroPorId(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Buscar Livros disponíveis com paginação")
    @GetMapping()
    public ResponseEntity<Page<LivroResponseDTO>> buscarLivrosDisponiveis(@PageableDefault(size = 10) Pageable page){
        Page<Livro> resultado = service.buscarLivrosDisponiveis(page);
        Page<LivroResponseDTO> dtoPage = resultado.map(l -> new LivroResponseDTO(l.getId(), l.getTitulo(), l.getAutor(), l.getIsbn(), l.getQuantidade()));
        return ResponseEntity.ok(dtoPage);
    }
}
