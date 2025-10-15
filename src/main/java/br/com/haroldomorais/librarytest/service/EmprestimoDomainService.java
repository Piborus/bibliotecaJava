package br.com.haroldomorais.librarytest.service;


import br.com.haroldomorais.librarytest.model.livro.Livro;
import br.com.haroldomorais.librarytest.model.usuario.Usuario;
import br.com.haroldomorais.librarytest.repository.EmprestimoRepository;
import br.com.haroldomorais.librarytest.repository.LivroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class EmprestimoDomainService {

    private final EmprestimoRepository repository;

    public void verificarDisponibilidadeLivro(Livro livro) {
        if(livro.getQuantidade() < 1){
            throw new RuntimeException("Livro sem unidades disponiveis.");
        }
    }

    public void verificarLimiteDeEmprestimosPorUsuario(Usuario usuario) {
        long emprestimosAtivos = repository.contarEmprestimosAtivosPorUsuario(usuario);
        if (emprestimosAtivos >= 5) {
            throw new RuntimeException("O usuario excedeu o limite de emprestimos ativos");
        }
    }

    public LocalDateTime calcularDataDeEntrega() {
        return LocalDateTime.now().plusDays(14);
    }
}
