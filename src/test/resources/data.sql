INSERT INTO role
VALUES (random_uuid(), 'admin'),
       (random_uuid(), 'user');
INSERT INTO authority
VALUES (random_uuid(), 'user:read'),
       (random_uuid(), 'user:write');
-- INSERT INTO role_authorities
-- VALUES ('admin', 'user:read'),
--        ('admin', 'user:write');

INSERT INTO users(id, username, password, full_name, email, iin, phone_number, status)
VALUES (random_uuid(), 'admin', 'password', 'Test User', 'test@example.com', '123456789012', '77777777777', 'ACTIVE')
