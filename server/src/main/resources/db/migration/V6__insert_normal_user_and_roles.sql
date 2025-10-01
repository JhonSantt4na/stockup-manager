-- Inserir usuário comum (user:user123)
INSERT INTO users (
    id, full_name, username, email, password, created_at
) VALUES (
    gen_random_uuid(),
    'Usuário Comum',
    'user',
    'user@stockup.com',
    '{pbkdf2}017551843aebf97ebe1c6c87463fed7095c2245f845e494d3d3b02b3a97b6c12dfee8f58306f750c',
    CURRENT_TIMESTAMP
)
ON CONFLICT (username) DO NOTHING;

-- Vincular usuário à role USER
INSERT INTO user_role (id_user, id_roles)
SELECT u.id, r.id
FROM users u
JOIN role r ON r.name = 'USER'
WHERE u.username = 'user'
ON CONFLICT (id_user, id_roles) DO NOTHING;