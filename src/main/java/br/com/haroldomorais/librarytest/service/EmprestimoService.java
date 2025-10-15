package br.com.haroldomorais.librarytest.service;

import br.com.haroldomorais.librarytest.model.emprestimo.Emprestimo;
import br.com.haroldomorais.librarytest.model.livro.Livro;
import br.com.haroldomorais.librarytest.model.livro.dto.LivroDTO;
import br.com.haroldomorais.librarytest.model.usuario.Usuario;
import br.com.haroldomorais.librarytest.repository.EmprestimoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmprestimoService {

    private final LivroService livroService;
    private final UsuarioService usuarioService;
    private final EmprestimoDomainService domainService;
    private final EmprestimoRepository repository;

    public void criaEmprestimo(List<Long> livroIds, Long usuarioId){
        Usuario usuario = usuarioService.buscarPorId(usuarioId);
        if (usuario == null) {
            throw new RuntimeException("Usuario não encontrado");
        }
        domainService.verificarLimiteDeEmprestimosPorUsuario(usuario);

        for (Long livroId : livroIds) {
            Livro livro = livroService.buscarPorId(livroId);

            if (livro == null) {
                throw new RuntimeException("Livro não encontrado");
            }

            domainService.verificarDisponibilidadeLivro(livro);

            livro.setQuantidade(livro.getQuantidade() - 1);
            livroService.atualizarLivro(livro.getId(), new LivroDTO());

            Emprestimo emprestimo = new Emprestimo();
            emprestimo.setDataDoEmprestimo(LocalDateTime.now());
            emprestimo.setDataDaDevolucao(null);
            emprestimo.setDataPrevistaDaDevolucao(domainService.calcularDataDeEntrega());
            emprestimo.setUsuario(usuario);
            emprestimo.setLivro(livro);

            repository.save(emprestimo);

        }
    }

    public void devolverLivro(List<Long> livroIds, Long usuarioId){
        Usuario usuario = usuarioService.buscarPorId(usuarioId);
        if (usuario == null) {
            throw new RuntimeException("Usuario não encontrado");
        }

        for (Long livroId : livroIds) {
            Livro livro = livroService.buscarPorId(livroId);

            if (livro == null) {
                throw new RuntimeException("Livro não encontrado");
            }

            List<Emprestimo> emprestimo = repository.findByUsuarioAndLivroAndDataDaDevolucaoIsNull(usuario, livro)
                    .orElseThrow(() -> new RuntimeException("Empréstimo não encontrado para o usuário e livro especificados"));

            if (emprestimo.isEmpty()) {
                throw new IllegalArgumentException("O livro " + livro.getTitulo() + " não foi emprestado para o usuário.");
            }
            for (Emprestimo emprestimo1 : emprestimo){
                emprestimo1.setDataDaDevolucao(LocalDateTime.now());

            }
            repository.saveAll(emprestimo);

            livro.setQuantidade(livro.getQuantidade() + 1);
            livroService.atualizarLivro(livro.getId(), new LivroDTO());
        }
    }

    public List<Emprestimo>buscarEmprestimosPorUsuario(Long usuarioId){
        Usuario usuario = usuarioService.buscarPorId(usuarioId);
        if (usuario == null) {
            throw new RuntimeException("Usuario não encontrado");
        }
        var busca = repository.buscarEmprestimosPorUsuario(usuarioId);
        return busca;
    }

    public List<Emprestimo>buscarEmprestimosEmAtrasoPorUsuario(Long usuarioId){
        Usuario usuario = usuarioService.buscarPorId(usuarioId);
        if (usuario == null){
            throw new RuntimeException("Usuario não encontrado");
        }

        var buscar = repository.buscarEmprestimosEmAtrasoPorUsuario(usuarioId);
        return buscar;
    }
}
