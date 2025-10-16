package br.com.haroldomorais.librarytest.factory;

import br.com.haroldomorais.librarytest.model.emprestimo.Emprestimo;
import br.com.haroldomorais.librarytest.model.livro.Livro;
import br.com.haroldomorais.librarytest.model.usuario.Usuario;
import br.com.haroldomorais.librarytest.service.EmprestimoDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class EmprestimoFactory {

    private final EmprestimoDomainService domainService;

    public Emprestimo criar(Usuario usuario, Livro livro, LocalDateTime agora) {
        Emprestimo emprestimo = new Emprestimo();
        emprestimo.setDataDoEmprestimo(agora);
        emprestimo.setDataDaDevolucao(null);
        emprestimo.setDataPrevistaDaDevolucao(domainService.calcularDataPrevistaDeDevolucao(agora));
        emprestimo.setUsuario(usuario);
        emprestimo.setLivro(livro);
        return emprestimo;
    }

    public List<Emprestimo> criarTodos(Usuario usuario, List<Livro> livros, LocalDateTime agora) {
        return livros.stream()
                .map(livro -> criar(usuario, livro, agora))
                .collect(Collectors.toList());
    }
}
