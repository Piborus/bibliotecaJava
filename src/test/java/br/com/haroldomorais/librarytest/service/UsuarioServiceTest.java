package br.com.haroldomorais.librarytest.service;

import br.com.haroldomorais.librarytest.exception.ResourceNotFoundException;
import br.com.haroldomorais.librarytest.model.usuario.Usuario;
import br.com.haroldomorais.librarytest.model.usuario.dto.UsuarioRequestDTO;
import br.com.haroldomorais.librarytest.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.Year;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UsuarioServiceTest {

    private UsuarioRepository repository;
    private UsuarioService service;

    @BeforeEach
    void setup() {
        repository = Mockito.mock(UsuarioRepository.class);
        service = new UsuarioService(repository);
    }

    @Test
    @DisplayName("cadastrarUsuario deve persistir usuario com matricula gerada")
    void cadastrarUsuario_devePersistirComMatricula() {
        UsuarioRequestDTO dto = new UsuarioRequestDTO();
        dto.setNome("Joao");
        dto.setEmail("joao@email.com");

        when(repository.save(any(Usuario.class))).thenAnswer(inv -> inv.getArgument(0));

        service.cadastrarUsuario(dto);

        ArgumentCaptor<Usuario> captor = ArgumentCaptor.forClass(Usuario.class);
        verify(repository).save(captor.capture());
        Usuario saved = captor.getValue();

        assertEquals("Joao", saved.getNome());
        assertEquals("joao@email.com", saved.getEmail());
        assertNotNull(saved.getMatricula());
        assertEquals(10, saved.getMatricula().length());
        assertTrue(saved.getMatricula().startsWith(String.valueOf(Year.now().getValue())));
    }

    @Test
    @DisplayName("buscarPorId deve retornar usuario quando existe")
    void buscarPorId_quandoExiste_retorna() {
        Usuario u = new Usuario();
        u.setId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(u));

        Usuario result = service.buscarPorId(1L);
        assertEquals(1L, result.getId());
    }

    @Test
    @DisplayName("buscarPorId deve lançar ResourceNotFoundException quando não existe")
    void buscarPorId_quandoNaoExiste_lanca() {
        when(repository.findById(2L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.buscarPorId(2L));
    }

    @Test
    @DisplayName("atualizarUsuario deve modificar somente campos não-nulos")
    void atualizarUsuario_deveModificarSomenteCamposNaoNulos() {
        Usuario existente = new Usuario();
        existente.setId(5L);
        existente.setNome("Antigo");
        existente.setEmail("old@mail.com");
        existente.setMatricula("2025123456");

        when(repository.findById(5L)).thenReturn(Optional.of(existente));
        when(repository.save(any(Usuario.class))).thenAnswer(inv -> inv.getArgument(0));

        UsuarioRequestDTO dto = new UsuarioRequestDTO();
        dto.setNome("Novo");
        // email null -> não deve alterar

        Usuario updated = service.atualizarUsuario(5L, dto);

        assertEquals("Novo", updated.getNome());
        assertEquals("old@mail.com", updated.getEmail());
        verify(repository).save(any(Usuario.class));
    }

    @Test
    @DisplayName("buscarUsuarioPorNomeEmailMatricula deve delegar para findAll quando sem parâmetros")
    void buscarUsuarioPorNomeEmailMatricula_semParams_delegarFindAll() {
        Pageable page = PageRequest.of(0, 10);
        Page<Usuario> pageResult = new PageImpl<>(java.util.Collections.emptyList());
        when(repository.findAll(page)).thenReturn(pageResult);

        Page<Usuario> result = service.buscarUsuarioPorNomeEmailMatricula(null, null, null, page);
        assertNotNull(result);
        verify(repository).findAll(page);
    }

    @Test
    @DisplayName("buscarUsuarioPorNomeEmailMatricula deve delegar para query quando tiver parâmetros")
    void buscarUsuarioPorNomeEmailMatricula_comParams_delegarQuery() {
        Pageable page = PageRequest.of(0, 10);
        Page<Usuario> pageResult = new PageImpl<>(java.util.Collections.emptyList());
        when(repository.buscarPorNomeEEmailMatricula("a", "b", "c", page)).thenReturn(pageResult);

        Page<Usuario> result = service.buscarUsuarioPorNomeEmailMatricula("a", "b", "c", page);
        assertNotNull(result);
        verify(repository).buscarPorNomeEEmailMatricula("a", "b", "c", page);
    }
}
