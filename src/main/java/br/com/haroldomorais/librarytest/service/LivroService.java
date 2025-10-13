package br.com.haroldomorais.librarytest.service;

import br.com.haroldomorais.librarytest.model.livro.Livro;
import br.com.haroldomorais.librarytest.model.livro.dto.LivroDTO;
import br.com.haroldomorais.librarytest.repository.LivroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class LivroService {

    @Autowired
    private LivroRepository livroRepository;

    public void cadastrarLivro(LivroDTO livroDto){
    }
}
