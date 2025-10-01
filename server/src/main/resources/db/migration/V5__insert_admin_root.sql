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

-- Vincular usuário à role ADMIN
INSERT INTO user_role (id_user, id_roles)
SELECT u.id, r.id
FROM users u
JOIN role r ON r.name = 'ADMIN'
WHERE u.username = 'admin'
ON CONFLICT (id_user, id_roles) DO NOTHING;
