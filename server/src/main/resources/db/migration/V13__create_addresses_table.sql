CREATE TABLE addresses (
    id UUID PRIMARY KEY,
    customer_id UUID NOT NULL,

    street VARCHAR(255) NOT NULL,
    number VARCHAR(50) NOT NULL,
    complement VARCHAR(255),
    district VARCHAR(255) NOT NULL,
    city VARCHAR(255) NOT NULL,
    state VARCHAR(100) NOT NULL,
    zip_code VARCHAR(20) NOT NULL,
    country VARCHAR(100) NOT NULL,

    enabled BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP NULL,

    CONSTRAINT fk_addresses_customer
        FOREIGN KEY (customer_id)
        REFERENCES customers(id)
        ON DELETE CASCADE
);

CREATE INDEX idx_addresses_customer ON addresses(customer_id);
CREATE INDEX idx_addresses_city ON addresses(city);
CREATE INDEX idx_addresses_enabled ON addresses(enabled);

-- Endereço padrão vinculado ao cliente padrão criado na migration anterior
INSERT INTO addresses (
    id, customer_id,
    street, number, complement, district, city, state, zip_code, country,
    enabled
) VALUES (
    'cccccccc-cccc-cccc-cccc-cccccccccccc',
    'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb',
    'Rua Exemplo',
    '123',
    'Casa',
    'Centro',
    'Cidade Exemplo',
    'Estado Exemplo',
    '00000-000',
    'Brasil',
    TRUE
);