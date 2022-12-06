DELETE FROM user_roles;
DELETE FROM comments;
DELETE FROM entries;
DELETE FROM users;

INSERT INTO users(name, email, password, description)
VALUES ('admin', 'admin@yandex.ru', '{noop}admin123', 'admin_description'),
       ('user', 'user@yandex.ru', '{noop}user123', 'user_description');

INSERT INTO user_roles(user_id, role)
VALUES (1, 'ADMIN'),
       (1, 'USER'),
       (2, 'USER');

INSERT INTO entries(user_id, title, content, updated)
VALUES (1, 'entry_1_title', 'entry_1_text', '2022-09-10 04:05:06'),
       (1, 'entry_2_title', 'entry_2_text', default),
       (2, 'entry_3_title', 'entry_3_text', '2022-09-25 09:12:55'),
       (2, 'entry_4_title', 'entry_4_text', default);

INSERT INTO comments(user_id, entry_id, text, posted)
VALUES (1, 1, 'admin_text_1', '2022-09-10 04:15:22'),
       (2, 1, 'user_text_1', '2022-09-15 10:05:00'),
       (2, 4, 'user_text_2', default);