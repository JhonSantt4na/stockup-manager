-- Inserir permissões SELLER e MANAGER
INSERT INTO permission (description)
VALUES
    ('SELLER'),
    ('MANAGER')
ON CONFLICT (description) DO NOTHING;

-- Inserir usuário user_normal com senha user123 (PBKDF2)
INSERT INTO users (
    id, full_name, username, email, password, created_at
) VALUES (
    gen_random_uuid(),
    'user common',
    'user',
    'user@stockup.com',
    '{pbkdf2}017551843aebf97ebe1c6c87463fed7095c2245f845e494d3d3b02b3a97b6c12dfee8f58306f750c',
    CURRENT_TIMESTAMP
)
ON CONFLICT (username) DO NOTHING;