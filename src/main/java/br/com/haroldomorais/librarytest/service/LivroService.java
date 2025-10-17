package br.com.haroldomorais.librarytest.service;

import br.com.haroldomorais.librarytest.exception.NotFoundException;
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
        livro.setIsbn(gerarIsbn());
        livro.setQuantidade(livroDto.getQuantidade());
        livroRepository.save(livro);
    }

    private String gerarIsbn() {
        StringBuilder sb = new StringBuilder("978");
        java.util.concurrent.ThreadLocalRandom rnd = java.util.concurrent.ThreadLocalRandom.current();
        for (int i = 0; i < 9; i++) {
            sb.append(rnd.nextInt(10));
        }
        String withoutCheck = sb.toString();
        int sum = 0;
        for (int i = 0; i < withoutCheck.length(); i++) {
            int d = Character.getNumericValue(withoutCheck.charAt(i));
            sum += (i % 2 == 0) ? d : d * 3;
        }
        int mod = sum % 10;
        int check = (mod == 0) ? 0 : 10 - mod;
        return withoutCheck + check;
    }

    public Livro buscarPorId(Long id){
        return livroRepository.findById(id).orElseThrow(() -> new NotFoundException("Livro não encontrado"));
    }

    public Livro buscarPorIsbn(String isbn) {
        return livroRepository.findByIsbn(isbn)
                .orElseThrow(() -> new NotFoundException("Livro não encontrado"));
    }

    public Livro atualizarLivro(Long id, LivroRequestDTO livroDto){
        Livro livro = livroRepository.findById(id).orElseThrow(() -> new NotFoundException("Livro não encontrado"));
        if (livroDto.getTitulo() != null) {
            livro.setTitulo(livroDto.getTitulo());
        }
        if (livroDto.getAutor() != null) {
            livro.setAutor(livroDto.getAutor());
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
