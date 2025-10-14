CREATE TABLE livros(
    id BIGSERIAL PRIMARY KEY,
    titulo VARCHAR(255) NOT NULL,
    autor VARCHAR(255) NOT NULL,
    isbn VARCHAR(255) NOT NULL UNIQUE,
    quantidade INTEGER CHECK ( quantidade IS NULL OR quantidade >= 0)
)