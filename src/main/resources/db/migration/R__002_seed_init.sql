INSERT INTO app_user (username, name, password_hash, is_active, deleted)
VALUES (
    'admin',
    'admin',
    '$2a$12$JkVoicP2mbRbl/f5VEdCDOMfocOcQNpEoblp.KnVKwsvMS4PFDnNy',
    TRUE,
    FALSE
)
ON CONFLICT (username) DO UPDATE SET
    name          = EXCLUDED.name,
    password_hash = EXCLUDED.password_hash,
    is_active     = EXCLUDED.is_active,
    deleted       = EXCLUDED.deleted;

INSERT INTO app_user_role (user_id, role_id)
SELECT u.id, r.id
FROM app_user u
JOIN role r ON r.role_name = 'ROLE_ADMIN'
WHERE u.username = 'admin'
ON CONFLICT (user_id, role_id) DO NOTHING;