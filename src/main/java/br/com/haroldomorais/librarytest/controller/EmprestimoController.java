package br.com.haroldomorais.librarytest.controller;

import br.com.haroldomorais.librarytest.model.emprestimo.dto.EmprestimoResumoDTO;
import br.com.haroldomorais.librarytest.service.EmprestimoService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/emprestimos")
@RequiredArgsConstructor
public class EmprestimoController {

    private final EmprestimoService service;

    @Operation(summary = "Emprestimo de Livro")
    @PostMapping
    public ResponseEntity<Void> emprestarLivro(@RequestParam List<Long> livroIds,@RequestParam Long usuarioId){
        service.criaEmprestimo(livroIds, usuarioId);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(summary = "Devolver Livro")
    @PatchMapping
    public ResponseEntity<Void> devolverLivro(@RequestParam List<Long> livroIds, @RequestParam Long usuarioId){
        service.devolverLivro(livroIds, usuarioId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Buscar Emprestimos por Usuario")
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<EmprestimoResumoDTO>> buscarEmprestimosPorUsuario(@PathVariable Long usuarioId){
        List<EmprestimoResumoDTO> resultado = service.buscarEmprestimosPorUsuario(usuarioId);
        return ResponseEntity.ok(resultado);
    }

    @Operation(summary = "Buscar Emprestimos em Atraso por Usuario")
    @GetMapping("/usuario/{usuarioId}/em-atraso")
    public ResponseEntity<List<EmprestimoResumoDTO>> buscarEmprestimosEmAtrasoPorUsuario(@PathVariable Long usuarioId){
        List<EmprestimoResumoDTO> resultado = service.buscarEmprestimosEmAtrasoPorUsuario(usuarioId);
        return ResponseEntity.ok(resultado);
    }
}
