INSERT INTO roles (name)
VALUES ('ROLE_ADMIN'), ('ROLE_STUDENT')
ON CONFLICT (name) DO NOTHING;

INSERT INTO users (username, password)
VALUES
    ('student' ,'$2a$10$VngJe1.o4n4eyyLymh9YVeOTtuQFyzPnAjSXX24cuTxST9EroEBJO'),
    ('admin' ,'$2a$10$VngJe1.o4n4eyyLymh9YVeOTtuQFyzPnAjSXX24cuTxST9EroEBJO')
ON CONFLICT (username) DO NOTHING;

INSERT INTO user_roles (user_id, role_id)
VALUES
    ((SELECT id FROM users WHERE username = 'student'), (SELECT id FROM roles WHERE name = 'ROLE_STUDENT')),
    ((SELECT id FROM users WHERE username = 'admin'), (SELECT id FROM roles WHERE name = 'ROLE_ADMIN'))
ON CONFLICT (user_id, role_id) DO NOTHING;

INSERT INTO students (user_id, fan_page_url, portfolio_url)
VALUES
    ((SELECT id FROM users WHERE username = 'student'), 'google.com', 'squirrels.com')
ON CONFLICT (user_id) DO NOTHING;
