package br.com.haroldomorais.librarytest.service;

import br.com.haroldomorais.librarytest.exception.BusinessException;
import br.com.haroldomorais.librarytest.model.livro.Livro;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class EmprestimoDomainService {

    public void verificarDisponibilidadeLivro(Livro livro) {
        if (livro.getQuantidade() < 1) {
            throw new BusinessException("Livro sem unidades disponiveis.");
        }
    }

    public void verificarLimiteDeEmprestimosPorUsuario(long emprestimosAtivos) {
        if (emprestimosAtivos >= 5) {
            throw new BusinessException("O usuario excedeu o limite de emprestimos ativos");
        }
    }

    public LocalDateTime calcularDataPrevistaDeDevolucao(LocalDateTime agora) {
        return agora.plusDays(14);
    }
}
