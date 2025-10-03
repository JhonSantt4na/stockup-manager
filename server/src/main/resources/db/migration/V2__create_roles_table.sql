CREATE TABLE IF NOT EXISTS roles (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(100) UNIQUE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    deleted_at TIMESTAMP,
    enabled BOOLEAN DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS role_permission (
    id_role UUID NOT NULL,
    id_permission UUID NOT NULL,
    PRIMARY KEY (id_role, id_permission),
    CONSTRAINT fk_role_permission_role FOREIGN KEY (id_role) REFERENCES roles(id) ON DELETE CASCADE,
    CONSTRAINT fk_role_permission_permission FOREIGN KEY (id_permission) REFERENCES permissions(id) ON DELETE CASCADE
);