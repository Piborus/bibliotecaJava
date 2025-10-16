package br.com.haroldomorais.librarytest.controller;

import br.com.haroldomorais.librarytest.model.usuario.Usuario;
import br.com.haroldomorais.librarytest.model.usuario.dto.UsuarioRequestDTO;
import br.com.haroldomorais.librarytest.model.usuario.dto.UsuarioResponseDTO;
import br.com.haroldomorais.librarytest.service.UsuarioService;
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
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @Operation(summary = "Cadastrar Usuario")
    @PostMapping()
    public ResponseEntity<Void> cadastrarUsuario(@RequestBody @Valid UsuarioRequestDTO usuario){
        usuarioService.cadastrarUsuario(usuario);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(summary = "Buscar Usuario por Id")
    @GetMapping("{id}")
    public ResponseEntity<UsuarioResponseDTO> buscarUsuarioPorId(@PathVariable Long id){
        Usuario u = usuarioService.buscarPorId(id);
        UsuarioResponseDTO dto = new UsuarioResponseDTO(u.getId(), u.getNome(), u.getEmail(), u.getMatricula());
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Atualizar Usuario por Id")
    @PatchMapping("{id}")
    public ResponseEntity<UsuarioResponseDTO> atualizarUsuarioPorId(@PathVariable Long id, @RequestBody @Valid UsuarioRequestDTO usuario) {
        Usuario updated = usuarioService.atualizarUsuario(id, usuario);
        UsuarioResponseDTO dto = new UsuarioResponseDTO(updated.getId(), updated.getNome(), updated.getEmail(), updated.getMatricula());
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Deletar Usuario por Id")
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deletarUsuarioPorId(@PathVariable Long id){
        usuarioService.deletarUsuario(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Buscar Usuario por Nome, Email ou Matrícula com paginação")
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
