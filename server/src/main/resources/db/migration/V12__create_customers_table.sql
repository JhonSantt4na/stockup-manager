CREATE TABLE customers (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    cpf_cnpj VARCHAR(20),
    phone VARCHAR(50),
    email VARCHAR(255),
    enabled BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP NULL
);

CREATE INDEX idx_customers_name ON customers(name);
CREATE INDEX idx_customers_enabled ON customers(enabled);

-- Inserindo um cliente padrão para testes
INSERT INTO customers (
    id, name, cpf_cnpj, phone, email, enabled
) VALUES (
    'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb',
    'Cliente Padrão',
    '00000000000',
    '0000000000',
    'cliente@exemplo.com',
    TRUE
);
