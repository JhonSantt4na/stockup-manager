-- Garante que a role ADMIN exista
INSERT INTO permission (description, created_at)
SELECT 'ADMIN', CURRENT_TIMESTAMP
WHERE NOT EXISTS (
    SELECT 1 FROM permission WHERE description = 'ADMIN'
);

-- Cria o usuário root admin
INSERT INTO users (
    id, username, email, password, created_at,
    account_non_expired, account_non_locked,
    credentials_non_expired, enabled
) VALUES (
    gen_random_uuid(),
    'admin',
    'admin@stockup.com',
    '{pbkdf2}c466bcc7a88fb42fc0c4b8947138f04c5ddb5ecfc64f3bad61750ef6be0502cead87f3dd03d8795f',  -- admin123
    CURRENT_TIMESTAMP,
    TRUE, TRUE, TRUE, TRUE
)
ON CONFLICT (username) DO NOTHING;

-- Vincula o usuário admin à role ADMIN
INSERT INTO user_permission (id_user, id_permission)
SELECT
    u.id,
    p.id
FROM users u
JOIN permission p ON p.description = 'ADMIN'
WHERE u.username = 'admin'
  AND NOT EXISTS (
    SELECT 1 FROM user_permission up
    WHERE up.id_user = u.id AND up.id_permission = p.id
);

