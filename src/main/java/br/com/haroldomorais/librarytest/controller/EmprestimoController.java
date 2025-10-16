package br.com.haroldomorais.librarytest.controller;

import br.com.haroldomorais.librarytest.model.emprestimo.dto.EmprestimoResumoDTO;
import br.com.haroldomorais.librarytest.service.EmprestimoService;
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

    @PostMapping
    public ResponseEntity<Void> emprestarLivro(@RequestParam List<Long> livroIds,@RequestParam Long usuarioId){
        service.criaEmprestimo(livroIds, usuarioId);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PatchMapping
    public ResponseEntity<Void> devolverLivro(@RequestParam List<Long> livroIds, @RequestParam Long usuarioId){
        service.devolverLivro(livroIds, usuarioId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<EmprestimoResumoDTO>> buscarEmprestimosPorUsuario(@PathVariable Long usuarioId){
        List<EmprestimoResumoDTO> resultado = service.buscarEmprestimosPorUsuario(usuarioId);
        return ResponseEntity.ok(resultado);
    }

    @GetMapping("/usuario/{usuarioId}/em-atraso")
    public ResponseEntity<List<EmprestimoResumoDTO>> buscarEmprestimosEmAtrasoPorUsuario(@PathVariable Long usuarioId){
        List<EmprestimoResumoDTO> resultado = service.buscarEmprestimosEmAtrasoPorUsuario(usuarioId);
        return ResponseEntity.ok(resultado);
    }
}
