package br.com.haroldomorais.librarytest.service;

import br.com.haroldomorais.librarytest.model.livro.Livro;
import br.com.haroldomorais.librarytest.model.livro.dto.LivroRequestDTO;
import br.com.haroldomorais.librarytest.repository.LivroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LivroService {

    private final LivroRepository livroRepository;

    public void cadastrarLivro(LivroRequestDTO livroDto){
        Livro livro = new Livro();
        livro.setTitulo(livroDto.getTitulo());
        livro.setAutor(livroDto.getAutor());
        livro.setIsbn(livroDto.getIsbn()); // Gera um ISBN aleatório
        livro.setQuantidade(livroDto.getQuantidade());
        livroRepository.save(livro);
    }

    public Livro buscarPorId(Long id){
        return livroRepository.findById(id).orElseThrow(() -> new RuntimeException("Livro não encontrado"));
    }

    public Livro atualizarLivro(Long id, LivroRequestDTO livroDto){
        Livro livro = livroRepository.findById(id).orElseThrow(() -> new RuntimeException("Livro não encontrado"));
        if (livroDto.getTitulo() != null) {
            livro.setTitulo(livroDto.getTitulo());
        }
        if (livroDto.getAutor() != null) {
            livro.setAutor(livroDto.getAutor());
        }
        if (livroDto.getIsbn() != null) {
            livro.setIsbn(livroDto.getIsbn());
        }
        if (livroDto.getQuantidade() != null) {
            livro.setQuantidade(livroDto.getQuantidade());
        }
        return livroRepository.save(livro);
    }

    public void deletarLivroPorId(Long id){
        livroRepository.deleteById(id);
    }

    public Page<Livro> buscarLivrosDisponiveis(Pageable page){
        return livroRepository.buscarLivrosDisponiveis(page);
    }


}
