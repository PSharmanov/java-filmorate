-- Заполнение таблицы mpa_rating
INSERT INTO mpa_rating (mpa_rating_name, description)
VALUES ('G', 'У фильма нет возрастных ограничений'),
       ('PG', 'Детям рекомендуется смотреть фильм с родителями'),
       ('PG-13', 'Детям до 13 лет просмотр не желателен'),
       ('R', 'Лицам до 17 лет просмотр разрешён только в присутствии взрослого'),
       ('NC-17', 'Лицам до 18 лет просмотр запрещён');

-- Заполнение таблицы genres
INSERT INTO genres (name)
VALUES ('Комедия'),
       ('Драма'),
       ('Мультфильм'),
       ('Триллер'),
       ('Документальный'),
       ('Боевик');

-- Заполнение таблицы status
INSERT INTO status (status_id, description) VALUES
('pending', 'дружба не подтверждена'),
('confirmed', 'дружба подтверждена'),
('blocked', 'дружба заблокирована'),
('removed', 'дружба удалена'),
('requested', 'запрос на дружбу отправлен');

--Заполнение таблицы users
INSERT INTO users (email, login, name, birthday, registrationDate) VALUES
('user1@example.com', 'user1', 'Имя пользователя 1', '1990-01-01', NOW()),
('user2@example.com', 'user2', 'Имя пользователя 2', '1985-05-15', NOW()),
('user3@example.com', 'user3', 'Имя пользователя 3', '1995-10-20', NOW());

--Заполнение таблицы friendship
INSERT INTO friendship (user_id, friend_id, status_id) VALUES
(1, 2, 'pending'),
(1, 3, 'requested'),
(2, 1, 'confirmed');

--Заполнение таблицы film
INSERT INTO film (name, description, releaseDate, duration, mpa_rating) VALUES
('Фильм 1', 'Описание фильма 1', '2023-03-15', 120, 1),
('Фильм 2', 'Описание фильма 2', '2023-04-20', 100, 2),
('Фильм 3', 'Описание фильма 3', '2023-05-10', 110, 3);

--Заполнение таблицы likes
INSERT INTO likes (film_id, user_id) VALUES
(1, 1),
(2, 2),
(3, 3);

--Заполнение таблицы film_genres
INSERT INTO film_genres (film_id, genre_id) VALUES
(1, 1),
(1, 2),
(2, 3),
(2, 4),
(3, 5),
(3, 6);
