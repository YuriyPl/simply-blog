DELETE FROM user_roles;
DELETE FROM comments;
DELETE FROM entries;
DELETE FROM users;
DELETE FROM refresh_token;

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

INSERT INTO refresh_token(user_id, token, expiry_date)
VALUES  (1, '9346087c-d972-4feb-8f75-3453e6d27fa2', '2023-12-14 01:00:00'),
        (2, 'f9dfbb1c-3cd3-4975-8790-7d2135bd7c0e', '2022-10-10 10:00:00'),
        (2, 'f209c28f-8b96-4837-b6e8-810730f7f959', '2023-10-10 10:00:00');