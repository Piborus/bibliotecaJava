package br.com.haroldomorais.librarytest.controller;

import br.com.haroldomorais.librarytest.model.usuario.Usuario;
import br.com.haroldomorais.librarytest.model.usuario.dto.UsuarioRequestDTO;
import br.com.haroldomorais.librarytest.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @PostMapping()
    public ResponseEntity<Void> cadastrarUsuario(@RequestBody @Valid UsuarioRequestDTO usuario){
        usuarioService.cadastrarUsuario(usuario);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    public ResponseEntity<Usuario> buscarUsuarioPorId(@PathVariable Long id){
        return ResponseEntity.ok(usuarioService.buscarPorId(id));
    }

    @PatchMapping("{id}")
    public ResponseEntity<Usuario> atualizarUsuarioPorId(@PathVariable Long id, @RequestBody UsuarioRequestDTO usuario) {
        return ResponseEntity.ok(usuarioService.atualizarUsuario(id, usuario));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deletarUsuarioPorId(@PathVariable Long id){
        usuarioService.deletarUsuario(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/page")
    public ResponseEntity<Page<Usuario>> buscarUsuarioPorNomeEmailMatricula(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String matricula,
            @PageableDefault(size = 10) Pageable page
    ){
        Page<Usuario> usuarios = usuarioService.buscarUsuarioPorNomeEmailMatricula(nome, email, matricula, page);
        return ResponseEntity.ok(usuarios);
    }
}
