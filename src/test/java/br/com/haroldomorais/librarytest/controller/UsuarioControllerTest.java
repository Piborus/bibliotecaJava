package br.com.haroldomorais.librarytest.controller;

import br.com.haroldomorais.librarytest.model.usuario.Usuario;
import br.com.haroldomorais.librarytest.model.usuario.dto.UsuarioRequestDTO;
import br.com.haroldomorais.librarytest.model.usuario.dto.UsuarioResponseDTO;
import br.com.haroldomorais.librarytest.service.UsuarioService;
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

class UsuarioControllerTest {

    private MockMvc mockMvc;
    private UsuarioService usuarioService;

    @BeforeEach
    void setup() {
        usuarioService = Mockito.mock(UsuarioService.class);
        UsuarioController controller = new UsuarioController(usuarioService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
    }

    @Test
    @DisplayName("POST /usuarios deve retornar 201 e chamar service.cadastrarUsuario")
    void cadastrarUsuario_deveRetornarCreated() throws Exception {
        mockMvc.perform(post("/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"nome\": \"Ana\", \"email\": \"a@b.com\" }"))
                .andExpect(status().isCreated());

        verify(usuarioService).cadastrarUsuario(any(UsuarioRequestDTO.class));
    }

    @Test
    @DisplayName("GET /usuarios/{id} deve retornar 200 com usuario")
    void buscarPorId_deveRetornarUsuario() throws Exception {
        Usuario u = new Usuario();
        u.setId(4L);
        u.setNome("User");
        u.setEmail("e@e.com");
        u.setMatricula("2025000001");

        when(usuarioService.buscarPorId(4L)).thenReturn(u);

        mockMvc.perform(get("/usuarios/{id}", 4L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(4)))
                .andExpect(jsonPath("$.nome", is("User")));

        verify(usuarioService).buscarPorId(4L);
    }

    @Test
    @DisplayName("GET /usuarios/matricula/{matricula} deve retornar 200 com usuario")
    void buscarPorMatricula_deveRetornarUsuario() throws Exception {
        Usuario u = new Usuario();
        u.setId(8L);
        u.setNome("MatUser");
        u.setMatricula("M123");

        when(usuarioService.buscarPorMatricula("M123")).thenReturn(u);

        mockMvc.perform(get("/usuarios/matricula/{matricula}", "M123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(8)))
                .andExpect(jsonPath("$.nome", is("MatUser")));

        verify(usuarioService).buscarPorMatricula("M123");
    }

    @Test
    @DisplayName("PATCH /usuarios/{id} deve chamar atualizar e retornar dto")
    void atualizarUsuario_deveChamarServico() throws Exception {
        Usuario updated = new Usuario();
        updated.setId(9L);
        updated.setNome("Upd");
        updated.setEmail("up@p.com");
        updated.setMatricula("2025123456");

        when(usuarioService.atualizarUsuario(eq(9L), any(UsuarioRequestDTO.class))).thenReturn(updated);

        mockMvc.perform(patch("/usuarios/{id}", 9L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"nome\": \"Upd\" }"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(9)))
                .andExpect(jsonPath("$.nome", is("Upd")));

        verify(usuarioService).atualizarUsuario(eq(9L), any(UsuarioRequestDTO.class));
    }

    @Test
    @DisplayName("DELETE /usuarios/{id} deve retornar 204 e chamar deletar")
    void deletarUsuario_deveChamarServico() throws Exception {
        mockMvc.perform(delete("/usuarios/{id}", 11L))
                .andExpect(status().isNoContent());

        verify(usuarioService).deletarUsuario(11L);
    }

    @Test
    @DisplayName("GET /usuarios/page deve retornar pagina vazia quando service delega")
    void buscarUsuarioPorNomeEmailMatricula_delegarPage() throws Exception {
        Page<Usuario> p = new PageImpl<>(Collections.emptyList());
        when(usuarioService.buscarUsuarioPorNomeEmailMatricula(any(), any(), any(), any())).thenReturn(p);

        mockMvc.perform(get("/usuarios/page"))
                .andExpect(status().isOk());

        verify(usuarioService).buscarUsuarioPorNomeEmailMatricula(any(), any(), any(), any());
    }
}
