-- Inserir permissões
INSERT INTO permission (id, description, created_at)
VALUES
    (gen_random_uuid(), 'FULL_ACCESS', CURRENT_TIMESTAMP),
    (gen_random_uuid(), 'BASIC_ACCESS', CURRENT_TIMESTAMP)
ON CONFLICT (description) DO NOTHING;

-- Inserir roles
INSERT INTO role (id, name, created_at)
VALUES
    (gen_random_uuid(), 'ADMIN', CURRENT_TIMESTAMP),
    (gen_random_uuid(), 'USER', CURRENT_TIMESTAMP)
ON CONFLICT (name) DO NOTHING;

-- Vincular ADMIN à permissão FULL_ACCESS
INSERT INTO roles_permission (id_role, id_permission)
SELECT r.id, p.id
FROM role r, permission p
WHERE r.name = 'ADMIN' AND p.description = 'FULL_ACCESS'
ON CONFLICT (id_role, id_permission) DO NOTHING;

-- Vincular USER à permissão BASIC_ACCESS
INSERT INTO roles_permission (id_role, id_permission)
SELECT r.id, p.id
FROM role r, permission p
WHERE r.name = 'USER' AND p.description = 'BASIC_ACCESS'
ON CONFLICT (id_role, id_permission) DO NOTHING;