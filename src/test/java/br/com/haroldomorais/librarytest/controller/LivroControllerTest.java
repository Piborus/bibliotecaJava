package br.com.haroldomorais.librarytest.controller;

import br.com.haroldomorais.librarytest.model.livro.Livro;
import br.com.haroldomorais.librarytest.model.livro.dto.LivroRequestDTO;
import br.com.haroldomorais.librarytest.service.LivroService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;

import java.util.Collections;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class LivroControllerTest {

    private MockMvc mockMvc;
    private LivroService livroService;

    @BeforeEach
    void setup() {
        livroService = Mockito.mock(LivroService.class);
        LivroController controller = new LivroController(livroService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
    }

    @Test
    @DisplayName("POST /livros deve retornar 201 e chamar service.cadastrarLivro")
    void cadastrarLivro_deveRetornarCreated() throws Exception {
        mockMvc.perform(post("/livros")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"titulo\": \"Livro A\", \"autor\": \"Autor A\", \"quantidade\": 3 }"))
                .andExpect(status().isCreated());

        verify(livroService).cadastrarLivro(any(LivroRequestDTO.class));
    }

    @Test
    @DisplayName("GET /livros/{id} deve retornar 200 com livro")
    void buscarPorId_deveRetornarLivro() throws Exception {
        Livro l = new Livro();
        l.setId(2L);
        l.setTitulo("Titulo Test");
        l.setAutor("Autor Test");
        l.setIsbn("9780123456789");
        l.setQuantidade(5);

        when(livroService.buscarPorId(2L)).thenReturn(l);

        mockMvc.perform(get("/livros/{id}", 2L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(2)))
                .andExpect(jsonPath("$.titulo", is("Titulo Test")));

        verify(livroService).buscarPorId(2L);
    }

    @Test
    @DisplayName("GET /livros/isbn/{isbn} deve retornar 200 com livro")
    void buscarPorIsbn_deveRetornarLivro() throws Exception {
        Livro l = new Livro();
        l.setId(3L);
        l.setTitulo("ISBN Livro");
        l.setIsbn("9780000000000");

        when(livroService.buscarPorIsbn("9780000000000")).thenReturn(l);

        mockMvc.perform(get("/livros/isbn/{isbn}", "9780000000000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(3)))
                .andExpect(jsonPath("$.titulo", is("ISBN Livro")));

        verify(livroService).buscarPorIsbn("9780000000000");
    }

    @Test
    @DisplayName("PATCH /livros/{id} deve chamar atualizar e retornar dto")
    void atualizarLivro_deveChamarServico() throws Exception {
        Livro updated = new Livro();
        updated.setId(5L);
        updated.setTitulo("UpdTitle");
        updated.setAutor("UpdAuthor");
        updated.setIsbn("9781111111111");
        updated.setQuantidade(2);

        when(livroService.atualizarLivro(eq(5L), any(LivroRequestDTO.class))).thenReturn(updated);

        mockMvc.perform(patch("/livros/{id}", 5L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"titulo\": \"UpdTitle\", \"autor\": \"UpdAuthor\", \"quantidade\": 2 }"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(5)))
                .andExpect(jsonPath("$.titulo", is("UpdTitle")));

        verify(livroService).atualizarLivro(eq(5L), any(LivroRequestDTO.class));
    }

    @Test
    @DisplayName("DELETE /livros/{id} deve retornar 204 e chamar deletar")
    void deletarLivro_deveChamarServico() throws Exception {
        mockMvc.perform(delete("/livros/{id}", 12L))
                .andExpect(status().isNoContent());

        verify(livroService).deletarLivroPorId(12L);
    }

    @Test
    @DisplayName("GET /livros deve retornar pagina de livros disponiveis")
    void buscarLivrosDisponiveis_deveRetornarPagina() throws Exception {
        Page<Livro> p = new PageImpl<>(Collections.emptyList());
        when(livroService.buscarLivrosDisponiveis(any())).thenReturn(p);

        mockMvc.perform(get("/livros").param("page", "0").param("size", "10").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(livroService).buscarLivrosDisponiveis(any());
    }
}
