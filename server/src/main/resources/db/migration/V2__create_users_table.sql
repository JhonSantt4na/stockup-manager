-- Criação da tabela de usuários
CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    username VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    password_hash VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP,
    account_non_expired BOOLEAN DEFAULT TRUE,
    account_non_locked BOOLEAN DEFAULT TRUE,
    credentials_non_expired BOOLEAN DEFAULT TRUE,
    enabled BOOLEAN DEFAULT TRUE
);

-- Tabela de relacionamento Many-to-Many entre users e permissions
CREATE TABLE user_permission (
    id_user UUID NOT NULL,
    id_permission BIGINT NOT NULL,
    PRIMARY KEY (id_user, id_permission),
    CONSTRAINT fk_user FOREIGN KEY (id_user) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_permission FOREIGN KEY (id_permission) REFERENCES permission(id) ON DELETE CASCADE
);
