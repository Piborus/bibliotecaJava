package br.com.haroldomorais.librarytest.service;

import br.com.haroldomorais.librarytest.exception.NotFoundException;
import br.com.haroldomorais.librarytest.model.livro.Livro;
import br.com.haroldomorais.librarytest.model.livro.dto.LivroRequestDTO;
import br.com.haroldomorais.librarytest.repository.LivroRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LivroServiceTest {

    private LivroRepository repository;
    private LivroService service;

    @BeforeEach
    void setup() {
        repository = Mockito.mock(LivroRepository.class);
        service = new LivroService(repository);
    }

    @Test
    @DisplayName("cadastrarLivro deve persistir com ISBN gerado começando com 978 e tamanho 13")
    void cadastrarLivro_geraIsbnValido() {
        LivroRequestDTO dto = new LivroRequestDTO();
        dto.setTitulo("Titulo");
        dto.setAutor("Autor");
        dto.setQuantidade(3);

        when(repository.save(any(Livro.class))).thenAnswer(inv -> inv.getArgument(0));

        service.cadastrarLivro(dto);

        ArgumentCaptor<Livro> captor = ArgumentCaptor.forClass(Livro.class);
        verify(repository).save(captor.capture());
        Livro saved = captor.getValue();

        assertEquals("Titulo", saved.getTitulo());
        assertEquals("Autor", saved.getAutor());
        assertNotNull(saved.getIsbn());
        assertTrue(saved.getIsbn().startsWith("978"));
        assertEquals(13, saved.getIsbn().length());
    }

    @Test
    @DisplayName("buscarPorId deve retornar quando existe")
    void buscarPorId_quandoExiste_retorna() {
        Livro l = new Livro();
        l.setId(2L);
        when(repository.findById(2L)).thenReturn(Optional.of(l));

        Livro result = service.buscarPorId(2L);
        assertEquals(2L, result.getId());
    }

    @Test
    @DisplayName("buscarPorId deve lançar ResourceNotFoundException quando não existe")
    void buscarPorId_quandoNaoExiste_lanca() {
        when(repository.findById(3L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> service.buscarPorId(3L));
    }

    @Test
    @DisplayName("atualizarLivro deve alterar somente campos não nulos e salvar")
    void atualizarLivro_deveAlterarSomenteCamposNaoNulos() {
        Livro existente = new Livro();
        existente.setId(7L);
        existente.setTitulo("Old");
        existente.setAutor("OldAuthor");
        existente.setQuantidade(5);

        when(repository.findById(7L)).thenReturn(Optional.of(existente));
        when(repository.save(any(Livro.class))).thenAnswer(inv -> inv.getArgument(0));

        LivroRequestDTO dto = new LivroRequestDTO();
        dto.setTitulo("NewTitle");
        // autor null
        dto.setQuantidade(10);

        Livro updated = service.atualizarLivro(7L, dto);

        assertEquals("NewTitle", updated.getTitulo());
        assertEquals("OldAuthor", updated.getAutor());
        assertEquals(10, updated.getQuantidade());
        verify(repository).save(any(Livro.class));
    }

    @Test
    @DisplayName("buscarLivrosDisponiveis deve delegar para repository")
    void buscarLivrosDisponiveis_deveDelegar() {
        Pageable page = PageRequest.of(0, 10);
        Page<Livro> p = new PageImpl<>(java.util.List.of());
        when(repository.buscarLivrosDisponiveis(page)).thenReturn(p);

        Page<Livro> result = service.buscarLivrosDisponiveis(page);
        assertNotNull(result);
        verify(repository).buscarLivrosDisponiveis(page);
    }
}

