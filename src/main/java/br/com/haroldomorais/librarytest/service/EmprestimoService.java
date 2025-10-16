package br.com.haroldomorais.librarytest.service;

import br.com.haroldomorais.librarytest.exception.ResourceNotFoundException;
import br.com.haroldomorais.librarytest.factory.EmprestimoFactory;
import br.com.haroldomorais.librarytest.model.emprestimo.Emprestimo;
import br.com.haroldomorais.librarytest.model.emprestimo.dto.EmprestimoResumoDTO;
import br.com.haroldomorais.librarytest.model.livro.Livro;
import br.com.haroldomorais.librarytest.model.livro.dto.LivroRequestDTO;
import br.com.haroldomorais.librarytest.model.usuario.Usuario;
import br.com.haroldomorais.librarytest.repository.EmprestimoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmprestimoService {

    private final LivroService livroService;
    private final UsuarioService usuarioService;
    private final EmprestimoDomainService domainService;
    private final EmprestimoRepository repository;
    private final EmprestimoFactory emprestimoFactory;

    public void criaEmprestimo(List<Long> livroIds, Long usuarioId) {
        Usuario usuario = usuarioService.buscarPorId(usuarioId);
        if (usuario == null) {
            throw new ResourceNotFoundException("Usuario não encontrado");
        }

        long emprestimosAtivos = repository.contarEmprestimosAtivosPorUsuario(usuario);
        domainService.verificarLimiteDeEmprestimosPorUsuario(emprestimosAtivos);

        List<Livro> livrosParaEmprestimo = new ArrayList<>();
        for (Long livroId : livroIds) {
            Livro livro = livroService.buscarPorId(livroId);
            if (livro == null) {
                throw new ResourceNotFoundException("Livro não encontrado");
            }

            domainService.verificarDisponibilidadeLivro(livro);

            // Atualiza estoque
            livro.setQuantidade(livro.getQuantidade() - 1);
            livroService.atualizarLivro(livro.getId(), new LivroRequestDTO());

            livrosParaEmprestimo.add(livro);
        }

        LocalDateTime agora = LocalDateTime.now();
        List<Emprestimo> emprestimos = emprestimoFactory.criarTodos(usuario, livrosParaEmprestimo, agora);
        repository.saveAll(emprestimos);
    }

    public void devolverLivro(List<Long> livroIds, Long usuarioId) {
        Usuario usuario = usuarioService.buscarPorId(usuarioId);
        if (usuario == null) {
            throw new ResourceNotFoundException("Usuario não encontrado");
        }

        for (Long livroId : livroIds) {
            Livro livro = livroService.buscarPorId(livroId);
            if (livro == null) {
                throw new ResourceNotFoundException("Livro não encontrado");
            }

            List<Emprestimo> emprestimo = repository.findByUsuarioAndLivroAndDataDaDevolucaoIsNull(usuario, livro)
                    .orElseThrow(() -> new ResourceNotFoundException("Empréstimo não encontrado para o usuário e livro especificados"));

            if (emprestimo.isEmpty()) {
                throw new IllegalArgumentException("O livro " + livro.getTitulo() + " não foi emprestado para o usuário.");
            }

            for (Emprestimo e : emprestimo) {
                e.setDataDaDevolucao(LocalDateTime.now());
            }

            repository.saveAll(emprestimo);

            livro.setQuantidade(livro.getQuantidade() + 1);
            livroService.atualizarLivro(livro.getId(), new LivroRequestDTO());
        }
    }

    public List<EmprestimoResumoDTO> buscarEmprestimosPorUsuario(Long usuarioId) {
        Usuario usuario = usuarioService.buscarPorId(usuarioId);
        if (usuario == null) {
            throw new ResourceNotFoundException("Usuario não encontrado");
        }
        return repository.buscarResumoEmprestimosPorUsuario(usuarioId);
    }

    public List<EmprestimoResumoDTO> buscarEmprestimosEmAtrasoPorUsuario(Long usuarioId) {
        Usuario usuario = usuarioService.buscarPorId(usuarioId);
        if (usuario == null) {
            throw new ResourceNotFoundException("Usuario não encontrado");
        }
        return repository.buscarResumoEmprestimosEmAtrasoPorUsuario(usuarioId);
    }
}
