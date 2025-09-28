-- Inserir permissão ADMIN
INSERT INTO permission (description)
VALUES ('ADMIN')
ON CONFLICT (description) DO NOTHING;

-- Inserir usuário admin
INSERT INTO users (
    id, full_name, username, email, password, created_at
) VALUES (
    gen_random_uuid(),
    'Administrador',
    'admin',
    'admin@stockup.com',
    '{pbkdf2}c466bcc7a88fb42fc0c4b8947138f04c5ddb5ecfc64f3bad61750ef6be0502cead87f3dd03d8795f',
    CURRENT_TIMESTAMP
)
ON CONFLICT (username) DO NOTHING;

-- Vincular usuário à permissão
INSERT INTO user_permission (id_user, id_permission)
SELECT
    u.id,
    p.id
FROM users u
CROSS JOIN permission p
WHERE u.username = 'admin'
AND p.description = 'ADMIN'
ON CONFLICT (id_user, id_permission) DO NOTHING;