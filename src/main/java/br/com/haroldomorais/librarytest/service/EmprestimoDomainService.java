package br.com.haroldomorais.librarytest.service;


import br.com.haroldomorais.librarytest.model.livro.Livro;
import br.com.haroldomorais.librarytest.model.usuario.Usuario;
import br.com.haroldomorais.librarytest.repository.LivroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmprestimoDomainService {

    @Autowired
    private LivroRepository livroRepository;

    public void verificarDisponibilidadeLivro(Livro livro) {
        if(livro.getQuantidade() < 1){
            throw new RuntimeException("Livro sem unidades disponiveis.");
        }
    }
}
