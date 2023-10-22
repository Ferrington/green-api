INSERT INTO roles (name)
VALUES ('ROLE_ADMIN'), ('ROLE_STUDENT')
ON CONFLICT (name) DO NOTHING;

INSERT INTO users (username, password, set_password_uuid)
VALUES
    ('student' ,'$2a$10$VngJe1.o4n4eyyLymh9YVeOTtuQFyzPnAjSXX24cuTxST9EroEBJO', '68dfcf9b77544d26be7385c4d6f18b7b'),
    ('admin' ,'$2a$10$VngJe1.o4n4eyyLymh9YVeOTtuQFyzPnAjSXX24cuTxST9EroEBJO', null)
ON CONFLICT (username) DO NOTHING;

INSERT INTO user_roles (user_id, role_id)
VALUES
    ((SELECT id FROM users WHERE username = 'student'), (SELECT id FROM roles WHERE name = 'ROLE_STUDENT')),
    ((SELECT id FROM users WHERE username = 'admin'), (SELECT id FROM roles WHERE name = 'ROLE_ADMIN'))
ON CONFLICT (user_id, role_id) DO NOTHING;

INSERT INTO students (user_id, fan_page_url, github_url)
VALUES
    ((SELECT id FROM users WHERE username = 'student'), 'http://google.com', 'http://github.com/fishman')
ON CONFLICT (user_id) DO NOTHING;
