-- Role definitions
INSERT INTO roles (id, name) VALUES (1, 'ROLE_ADMIN');
INSERT INTO roles (id, name) VALUES (2, 'ROLE_USER');

-- Users (hasła są zahashowane — bcrypt: zaq1@WSX)
INSERT INTO users (id, username, password) VALUES (
    1,
    'admin',
    '$2a$12$/cK1bn///ydauSMCJ4uFQuKxGrhRTxef94hQJm7ImMmraJJZVXxKa'
);

INSERT INTO users (id, username, password) VALUES (
    2,
    'user',
    '$2a$12$/cK1bn///ydauSMCJ4uFQuKxGrhRTxef94hQJm7ImMmraJJZVXxKa'
);

-- numerowanie ID użytkowników od 3 po dodaniu powyższych
SELECT setval('users_id_seq', 2, true);

-- Many-to-many relationship via join table user_roles
INSERT INTO user_roles (user_id, role_id) VALUES (1, 1); -- admin → ROLE_ADMIN
INSERT INTO user_roles (user_id, role_id) VALUES (1, 2); -- admin → ROLE_USER
INSERT INTO user_roles (user_id, role_id) VALUES (2, 2); -- user → ROLE_USER
