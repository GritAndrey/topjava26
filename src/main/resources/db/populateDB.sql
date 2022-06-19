DELETE
FROM user_roles;
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
       ('Обед', 1000, '2020-01-31 13:00:00', 100000),
       ('Ужин', 1000, '2020-01-31 20:00:00', 100000);
--     save(new Meal(LocalDateTime.of(2015, Month.JUNE, 1, 14, 0), "Админ ланч", 510), ADMIN_ID);
-- save(new Meal(LocalDateTime.of(2015, Month.JUNE, 1, 21, 0), "Админ ужин", 1500), ADMIN_ID);
--     new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
--             new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
--             new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
--             new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
--             new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
--             new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
--             new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужи", 410)