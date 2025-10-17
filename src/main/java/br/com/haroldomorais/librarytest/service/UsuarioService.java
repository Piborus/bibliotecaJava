package br.com.haroldomorais.librarytest.service;

import br.com.haroldomorais.librarytest.exception.NotFoundException;
import br.com.haroldomorais.librarytest.model.usuario.Usuario;
import br.com.haroldomorais.librarytest.model.usuario.dto.UsuarioRequestDTO;
import br.com.haroldomorais.librarytest.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository repository;

    public void cadastrarUsuario(UsuarioRequestDTO usuario){
        Usuario newUsuario = new Usuario();
        newUsuario.setNome(usuario.getNome());
        newUsuario.setEmail(usuario.getEmail());
        newUsuario.setMatricula(gerarMatricula());
        repository.save(newUsuario);
    }

    private String gerarMatricula() {
        Random random = new Random();
        String ano = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy"));
        int numero = 100000 + random.nextInt(900000);
        return ano + numero;
    }

    public Usuario buscarPorId(Long id){
        return repository.findById(id).orElseThrow(() -> new NotFoundException("Usuário não encontrado"));
    }

    public Usuario buscarPorMatricula(String matricula) {
        return repository.findByMatricula(matricula)
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado"));
    }

    public Usuario atualizarUsuario(Long id, UsuarioRequestDTO usuario){
        Usuario buscarUsuario = repository.findById(id).orElseThrow(() -> new NotFoundException("Usuário não encontrado"));
        if (usuario.getNome() != null) {
            buscarUsuario.setNome(usuario.getNome());
        }
        if (usuario.getEmail() != null) {
            buscarUsuario.setEmail(usuario.getEmail());
        }
        return repository.save(buscarUsuario);
    }

    public  void deletarUsuario(Long id){
        repository.deleteById(id);
    }

    public Page<Usuario> buscarUsuarioPorNomeEmailMatricula(String nome, String email, String matricula, Pageable page){
        boolean noParams = (nome == null || nome.isBlank())
                && (email == null || email.isBlank())
                && (matricula == null || matricula.isBlank());
        if (noParams) {
            return repository.findAll(page);
        }
        return repository.buscarPorNomeEEmailMatricula(nome, email, matricula, page);
    }

}
