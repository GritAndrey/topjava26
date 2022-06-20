DELETE
FROM user_roles;
DELETE
FROM meals;
DELETE
FROM users;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password)
VALUES ('User', 'user@yandex.ru', 'password'),
       ('Admin', 'admin@gmail.com', 'admin'),
       ('Guest', 'guest@gmail.com', 'guest');

INSERT INTO user_roles (role, user_id)
VALUES ('USER', 100000),
       ('ADMIN', 100001);

INSERT INTO meals (description, calories, date_time, user_id)
VALUES ('Админ ланч', 510, '2020-06-1 14:00:00', 100001),
       ('Админ ужин', 1500, '2020-06-1 21:00:00', 100001),
       ('Завтрак', 500, '2020-01-30 10:00:00', 100000),
       ('Обед', 1000, '2020-01-30 13:00:00', 100000),
       ('Ужин', 500, '2020-01-30 20:00:00', 100000),
       ('Еда на граничное значение', 100, '2020-01-31 00:00:00', 100000),
       ('Завтрак', 1000, '2020-01-31 10:00:00', 100000),
       ('Обед', 500, '2020-01-31 13:00:00', 100000),
       ('Ужин', 410, '2020-01-31 20:00:00', 100000);
