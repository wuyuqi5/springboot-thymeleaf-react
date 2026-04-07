INSERT INTO role (role_name, role_description) VALUES
    ('ROLE_ADMIN',                       'admin'),
    ('ROLE_USER',                        'user')
ON CONFLICT (role_name) DO UPDATE SET
    role_description = EXCLUDED.role_description,
    updated_at       = CURRENT_TIMESTAMP;
