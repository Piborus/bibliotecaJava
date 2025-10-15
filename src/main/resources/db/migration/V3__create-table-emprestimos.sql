CREATE TABLE emprestimos(
    id BIGSERIAL PRIMARY KEY,
    data_do_emprestimo TIMESTAMP,
    data_da_devolucao TIMESTAMP,
    data_prevista_da_devolucao TIMESTAMP,
    usuario_id BIGINT NOT NULL,
    livro_id BIGINT NOT NULL,
    CONSTRAINT fk_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id),
    CONSTRAINT fk_livro FOREIGN KEY (livro_id) REFERENCES livros(id)
)