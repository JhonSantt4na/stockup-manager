CREATE TABLE products (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP NULL,
    enabled BOOLEAN DEFAULT TRUE,

    name VARCHAR(150) NOT NULL,
    sku VARCHAR(50) NOT NULL UNIQUE,
    gtin VARCHAR(20),
    description TEXT,

    unit_of_measure VARCHAR(20),

    cost_price NUMERIC(19,4),
    sale_price NUMERIC(19,4),

    cst VARCHAR(10),
    ncm VARCHAR(10),
    cfop VARCHAR(10),

    icms_rate NUMERIC(5,2),
    pis_rate NUMERIC(5,2),
    cofins_rate NUMERIC(5,2),
    ipi_rate NUMERIC(5,2),

    category_id UUID,
    tax_profile_id UUID,

    origin VARCHAR(50),
    status VARCHAR(50),

    CONSTRAINT fk_products_category
        FOREIGN KEY (category_id)
        REFERENCES category(id)
        ON DELETE SET NULL,

    CONSTRAINT fk_products_tax_profile
        FOREIGN KEY (tax_profile_id)
        REFERENCES tax_profiles(id)
        ON DELETE SET NULL
);

CREATE INDEX idx_products_name ON products(name);
CREATE INDEX idx_products_sku ON products(sku);
CREATE INDEX idx_products_enabled ON products(enabled);
CREATE INDEX idx_products_category_id ON products(category_id);
CREATE INDEX idx_products_tax_profile_id ON products(tax_profile_id);

INSERT INTO products (
    id, name, sku, gtin, description, unit_of_measure,
    cost_price, sale_price,
    cst, ncm, cfop,
    icms_rate, pis_rate, cofins_rate, ipi_rate,
    category_id, tax_profile_id,
    origin, status
)
VALUES (
    '11111111-1111-1111-1111-111111111111',
    'Produto de Exemplo', 'SKU-0001', '1234567890123',
    'Produto inicial de teste', 'UN',
    10.00, 19.90,
    '000', '00000000', '5102',
    18.00, 1.65, 7.60, 5.00,
    '11111111-1111-1111-1111-111111111100',
    '11111111-1111-1111-1111-111111111110',
    'NATIONAL', 'ACTIVE'
);