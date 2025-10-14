package br.com.haroldomorais.librarytest.service;

import br.com.haroldomorais.librarytest.model.usuario.Usuario;
import br.com.haroldomorais.librarytest.model.usuario.dto.UsuarioDTO;
import br.com.haroldomorais.librarytest.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {


    private final UsuarioRepository repository;


    public void cadastrarUsuario(UsuarioDTO usuario){
        Usuario newUsuario = new Usuario();
        newUsuario.setNome(usuario.getNome());
        newUsuario.setEmail(usuario.getEmail());
        newUsuario.setMatricula(usuario.getMatricula()); //cria uma algoritmo para gerar a matrícula
        repository.save(newUsuario);
    }


    public Usuario buscarPorId(Long id){
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    public Usuario atualizarUsuario(Long id, UsuarioDTO usuario){
        Usuario buscarUsuario = repository.findById(id).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        if (usuario.getNome() != null) {
            buscarUsuario.setNome(usuario.getNome());
        }
        if (usuario.getEmail() != null) {
            buscarUsuario.setEmail(usuario.getEmail());
        }
        if (usuario.getMatricula() != null) {
            buscarUsuario.setMatricula(usuario.getMatricula());
        }
        return repository.save(buscarUsuario);
    }

    public  void deletarUsuario(Long id){
        repository.deleteById(id);
    }


    public Page<Usuario> buscarUsuarioPorNomeEmailMatricula(String nome, String email, String matricula, Pageable page){
        return repository.buscarPorNomeEEmailMatricula(nome, email, matricula, page);
    }

}
