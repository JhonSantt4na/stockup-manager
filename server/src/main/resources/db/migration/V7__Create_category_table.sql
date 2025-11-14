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

INSERT INTO category (id, name, description) VALUES
    ('95043764-9254-4b71-a9dc-9c813897a300', 'Eletrônicos', 'Produtos eletrônicos em geral'),
    (gen_random_uuid(), 'Livros', 'Livros de diversos gêneros');