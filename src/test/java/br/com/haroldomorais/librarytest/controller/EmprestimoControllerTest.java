package br.com.haroldomorais.librarytest.controller;

import br.com.haroldomorais.librarytest.model.emprestimo.dto.EmprestimoResumoDTO;
import br.com.haroldomorais.librarytest.model.usuario.Usuario;
import br.com.haroldomorais.librarytest.service.EmprestimoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class EmprestimoControllerTest {

    private MockMvc mockMvc;
    private EmprestimoService emprestimoService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        emprestimoService = Mockito.mock(EmprestimoService.class);
        EmprestimoController controller = new EmprestimoController(emprestimoService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    @DisplayName("POST /emprestimos deve retornar 201 e chamar service.criaEmprestimo")
    void emprestarLivro_deveRetornarCreated() throws Exception {
        mockMvc.perform(post("/emprestimos")
                        .param("livroIds", "1", "2")
                        .param("usuarioId", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        Mockito.verify(emprestimoService).criaEmprestimo(List.of(1L, 2L), 10L);
    }

    @Test
    @DisplayName("PATCH /emprestimos deve retornar 200 e chamar service.devolverLivro")
    void devolverLivro_deveRetornarOk() throws Exception {
        mockMvc.perform(patch("/emprestimos")
                        .param("livroIds", "3")
                        .param("usuarioId", "7"))
                .andExpect(status().isOk());

        Mockito.verify(emprestimoService).devolverLivro(List.of(3L), 7L);
    }

    @Test
    @DisplayName("GET /emprestimos/usuario/{id} deve retornar 200 com lista de resumos")
    void buscarEmprestimosPorUsuario_deveRetornarLista() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setId(5L);
        LocalDateTime agora = LocalDateTime.of(2025, 1, 1, 10, 0);
        EmprestimoResumoDTO dto1 = new EmprestimoResumoDTO(1L, agora, agora.plusDays(14), null,
                usuario, 100L, "Titulo A", "Autor A", "ISBN-A", 1L);
        EmprestimoResumoDTO dto2 = new EmprestimoResumoDTO(2L, agora, agora.plusDays(14), null,
                usuario, 101L, "Titulo B", "Autor B", "ISBN-B", 1L);

        Mockito.when(emprestimoService.buscarEmprestimosPorUsuario(5L))
                .thenReturn(List.of(dto1, dto2));

        mockMvc.perform(get("/emprestimos/usuario/{usuarioId}", 5L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].titulo", is("Titulo A")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].titulo", is("Titulo B")));

        Mockito.verify(emprestimoService).buscarEmprestimosPorUsuario(5L);
    }

    @Test
    @DisplayName("GET /emprestimos/usuario/{id}/em-atraso deve retornar 200 com lista de resumos")
    void buscarEmprestimosEmAtrasoPorUsuario_deveRetornarLista() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setId(3L);
        LocalDateTime agora = LocalDateTime.of(2025, 1, 1, 10, 0);
        EmprestimoResumoDTO dto = new EmprestimoResumoDTO(10L, agora, agora.minusDays(1), null,
                usuario, 200L, "Titulo C", "Autor C", "ISBN-C", 1L);

        Mockito.when(emprestimoService.buscarEmprestimosEmAtrasoPorUsuario(3L))
                .thenReturn(List.of(dto));

        mockMvc.perform(get("/emprestimos/usuario/{usuarioId}/em-atraso", 3L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(10)))
                .andExpect(jsonPath("$[0].titulo", is("Titulo C")));

        Mockito.verify(emprestimoService).buscarEmprestimosEmAtrasoPorUsuario(3L);
    }
}
