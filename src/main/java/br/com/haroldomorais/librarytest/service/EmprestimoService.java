package br.com.haroldomorais.librarytest.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmprestimoService {

    private final LivroService livroService;
    private final UsuarioService usuarioService;
    private final EmprestimoDomainService domainService;

    public void emprestarLivro(List<Long> livroId, Long usuarioId){

    }
}
