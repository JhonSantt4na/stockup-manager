-- Create category table with all BaseEntity columns
CREATE TABLE category (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP NULL,
    enabled BOOLEAN DEFAULT TRUE,
    name VARCHAR(100) NOT NULL UNIQUE,
    description VARCHAR(255)
);

CREATE INDEX idx_category_name ON category(name);
CREATE INDEX idx_category_enabled ON category(enabled);
CREATE INDEX idx_category_deleted_at ON category(deleted_at);

INSERT INTO category (name, description) VALUES
    ('Eletrônicos', 'Produtos eletrônicos em geral - smartphones, tablets, laptops'),
    ('Livros', 'Livros de diversos gêneros - ficção, técnicos, didáticos');