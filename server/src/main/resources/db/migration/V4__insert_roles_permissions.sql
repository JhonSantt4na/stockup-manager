INSERT INTO permissions (id, description, created_at)
VALUES
    (gen_random_uuid(), 'FULL_ACCESS', CURRENT_TIMESTAMP),
    (gen_random_uuid(), 'BASIC_ACCESS', CURRENT_TIMESTAMP)
ON CONFLICT (description) DO NOTHING;

INSERT INTO roles (id, name, created_at)
VALUES
    (gen_random_uuid(), 'ADMIN', CURRENT_TIMESTAMP),
    (gen_random_uuid(), 'USER', CURRENT_TIMESTAMP)
ON CONFLICT (name) DO NOTHING;

INSERT INTO role_permission (id_role, id_permission)
SELECT r.id, p.id
FROM roles r
JOIN permissions p ON p.description = 'FULL_ACCESS'
WHERE r.name = 'ADMIN'
ON CONFLICT (id_role, id_permission) DO NOTHING;

INSERT INTO role_permission (id_role, id_permission)
SELECT r.id, p.id
FROM roles r
JOIN permissions p ON p.description = 'BASIC_ACCESS'
WHERE r.name = 'USER'
ON CONFLICT (id_role, id_permission) DO NOTHING;