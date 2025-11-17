CREATE TABLE tax_profiles (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP NULL,
    enabled BOOLEAN DEFAULT TRUE,

    name VARCHAR(100) NOT NULL UNIQUE,
    description VARCHAR(255),

    cst VARCHAR(10),
    ncm VARCHAR(10),
    cfop VARCHAR(10),

    icms_rate NUMERIC(5, 2),
    pis_rate NUMERIC(5, 2),
    cofins_rate NUMERIC(5, 2),
    ipi_rate NUMERIC(5, 2),

    regime VARCHAR(50)
);

CREATE INDEX idx_tax_profile_name ON tax_profiles(name);
CREATE INDEX idx_tax_profile_enabled ON tax_profiles(enabled);
CREATE INDEX idx_tax_profile_deleted_at ON tax_profiles(deleted_at);

INSERT INTO tax_profiles (
    id, name, description,
    cst, ncm, cfop,
    icms_rate, pis_rate, cofins_rate, ipi_rate,
    regime
)
VALUES (
    '11111111-1111-1111-1111-111111111110',
    'Padrão Nacional',
    'Perfil tributário padrão para produtos nacionais',
    '000', '00000000', '5102',
    18.00, 1.65, 7.60, 5.00,
    'Simples Nacional'
);