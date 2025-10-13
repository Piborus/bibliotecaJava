package br.com.haroldomorais.librarytest.controller;

import br.com.haroldomorais.librarytest.model.usuario.Usuario;
import br.com.haroldomorais.librarytest.model.usuario.dto.UsuarioDTO;
import br.com.haroldomorais.librarytest.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping()
    public ResponseEntity<Void> cadastrarUsuario(@RequestBody @Valid UsuarioDTO usuario){
        usuarioService.cadastrarUsuario(usuario);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    public ResponseEntity<Usuario> buscarPorId(@PathVariable Long id){
        return ResponseEntity.ok(usuarioService.buscarPorId(id));
    }

    @PatchMapping("{id}")
    public ResponseEntity<Usuario> atualizarUsuario(@PathVariable Long id, @RequestBody UsuarioDTO usuario) {
        return ResponseEntity.ok(usuarioService.atualizarUsuario(id, usuario));
    }
}
