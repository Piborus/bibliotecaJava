package br.com.haroldomorais.librarytest.model.emprestimo;

import br.com.haroldomorais.librarytest.model.livro.Livro;
import br.com.haroldomorais.librarytest.model.usuario.Usuario;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "emprestimos")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Emprestimo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "data_do_emprestimo", nullable = false)
    private LocalDateTime dataDoEmprestimo;

    @Column(name = "data_da_devolucao", nullable = false)
    private LocalDateTime dataDaDevolucao;

    @Column(name = "data_prevista_da_devolucao", nullable = false)
    private LocalDateTime dataPrevistaDaDevolucao;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "livro_id", nullable = false)
    private Livro livro;
}
