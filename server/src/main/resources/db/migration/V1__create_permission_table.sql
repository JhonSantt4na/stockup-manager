-- Criação da tabela de permissões
CREATE TABLE permission (
    id BIGSERIAL PRIMARY KEY,
    description VARCHAR(255) NOT NULL UNIQUE
);