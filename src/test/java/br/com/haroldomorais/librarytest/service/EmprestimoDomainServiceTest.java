package br.com.haroldomorais.librarytest.service;

import br.com.haroldomorais.librarytest.model.livro.Livro;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class EmprestimoDomainServiceTest {

    private final EmprestimoDomainService service = new EmprestimoDomainService();

    @Test
    @DisplayName("Deve lançar exceção quando livro não possui unidades disponíveis (quantidade < 1)")
    void verificarDisponibilidadeLivro_semUnidades_deveLancarExcecao() {
        Livro livro = new Livro();
        livro.setQuantidade(0);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.verificarDisponibilidadeLivro(livro));
        assertEquals("Livro sem unidades disponiveis.", ex.getMessage());
    }

    @Test
    @DisplayName("Não deve lançar exceção quando livro possui ao menos 1 unidade")
    void verificarDisponibilidadeLivro_comUnidades_devePassar() {
        Livro livro = new Livro();
        livro.setQuantidade(1);

        assertDoesNotThrow(() -> service.verificarDisponibilidadeLivro(livro));
    }

    @Test
    @DisplayName("Deve lançar exceção quando usuário já possui 5 empréstimos ativos")
    void verificarLimiteEmprestimos_quandoCinco_erro() {
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.verificarLimiteDeEmprestimosPorUsuario(5));
        assertEquals("O usuario excedeu o limite de emprestimos ativos", ex.getMessage());
    }

    @Test
    @DisplayName("Não deve lançar exceção quando usuário possui menos de 5 empréstimos ativos")
    void verificarLimiteEmprestimos_quandoQuatro_ok() {
        assertDoesNotThrow(() -> service.verificarLimiteDeEmprestimosPorUsuario(4));
    }

    @Test
    @DisplayName("Deve calcular data prevista de devolução como agora + 14 dias")
    void calcularDataPrevistaDeDevolucao_deveSomar14Dias() {
        LocalDateTime agora = LocalDateTime.of(2025, 1, 10, 8, 30, 0);
        LocalDateTime esperado = LocalDateTime.of(2025, 1, 24, 8, 30, 0);

        LocalDateTime resultado = service.calcularDataPrevistaDeDevolucao(agora);

        assertEquals(esperado, resultado);
    }
}
