# java-filmorate
Template repository for Filmorate project.

## ER схема базы данных для приложения Filmorate
![er_diagram.png](src%2Fdocs%2Fer_diagram.png)

### Описание базы данных:
#### Таблица **_user_** - содержит данные о пользователях.
Таблица включает такие поля:
 - **_user_id_** (первичный ключ, PK) - уникальный идентификатор пользователя;
 - **_email_** - электронная почта пользователя;
 - **_login_** - логин пользователя;
 - **_name_** - имя пользователя;
 - **_birthday_** - дата рождения пользователя;
 - **_registrationDate_** - дата регистрации пользователя.

#### Таблица **_film_** - содержит данные о фильмах.
Таблица включает такие поля:
- **_film_id_** (первичный ключ, PK) - уникальный идентификатор фильма;
- **_name_** - название фильма;
- **_description_** - описание фильма;
- **_releaseDate_** - дата выхода фильма;
- **_duration_** - продолжительность фильма;
- **_mpaRating_** (внешний ключ, FK. Отсылает к таблице mpa_rating) - идентификатор рейтинга фильма.

#### Таблица **_mpa_rating_** - содержит информацию о рейтингах фильмов.
Таблица включает такие поля:
- **_mpaRating_id_** (первичный ключ, PK) - уникальный идентификатор рейтинга фильма (например: G, PG, PG-13, R, NC-17);
- **_description_** - описание рейтинга (например: G — у фильма нет возрастных ограничений,
  PG — детям рекомендуется смотреть фильм с родителями, PG-13 — детям до 13 лет просмотр не желателен,
  R — лицам до 17 лет просматривать фильм можно только в присутствии взрослого, NC-17 — лицам до 18 лет просмотр запрещён.).

#### Таблица **_genres_** - содержит информацию о жанрах фильмов.
Таблица включает такие поля:
- **_genre_id_** (первичный ключ, PK) - уникальный идентификатор жанра фильма;
- **_name_** - название жанра (например: Комедия. Драма. Мультфильм. Триллер. Документальный. Боевик.).

#### Таблица **_film_genres_** - содержит данные о жанрах фильмов.
Таблица включает такие поля:
- **_id_** (первичный ключ, PK) - уникальный идентификатор;
- **_film_id_** (внешний ключ, FK. Отсылает к таблице film) - идентификатор фильма;
- **_genre_id_** (внешний ключ, FK. Отсылает к таблице genres) - идентификатор жанра.

#### Таблица **_friendship_** - содержит данные о запросах в друзья.
Таблица включает такие поля:
- **_friendship_id_** (первичный ключ, PK) - уникальный идентификатор запроса дружбы;
- **_user_id_** (внешний ключ, FK. Отсылает к таблице user) - идентификатор пользователя запросившего дружбу;
- **_friend_id_** (внешний ключ, FK. Отсылает к таблице user) - идентификатор пользователя получившего запрос на дружбу;
- **_status_id_** (внешний ключ, FK. Отсылает к таблице status) - идентификатор статуса дружбы дружбы.

#### Таблица **_status_** - содержит информацию о статусах дружбы.
Таблица включает такие поля:
- **_status_id_** (первичный ключ, PK) - уникальный идентификатор статуса дружбы (например: pending, confirmed);
- **_description_** - описание статуса дружбы (например: pending - дружба не подтверждена, confirmed - дружба подтверждена).

### Примеры запросов основных операций приложения:

#### FilmService

1. Получение данных по всем фильмам - findAll():
   SELECT * FROM film;

2. Добавление фильма в базу данных - create():
   INSERT INTO film (name, description, releaseDate, duration, mpaRating)
   VALUES ('New Film', 'This is a new film', '2022-01-01', 120, 1);

3. Обновление данных фильма в базе данных - update():
   UPDATE film
   SET name = 'Updated Film', description = 'This is an updated film'
   WHERE film_id = 1;

4. Получение данных о фильме по id - findById():
   SELECT * FROM film WHERE film_id = 1;

5. Добавление лайка фильму - addLike():
   INSERT INTO likes (film_id, user_id)
   VALUES (1, 1);

6. Удаление лайка фильма - removeLike():
   DELETE FROM likes WHERE film_id = 1 AND user_id = 1;
   
7. Получение 10 популярных фильмов - findPopular():
   SELECT f.*, COUNT(l.film_id) AS likes_count
   FROM film f
   JOIN likes l ON f.film_id = l.film_id
   GROUP BY f.film_id
   ORDER BY likes_count DESC
   LIMIT 10;

#### UserService

1. Получение всех данных о пользователях - findAll():
  SELECT * FROM user;

2. Добавление пользователя в базу данных - create():
   INSERT INTO user (email, login, name, birthday, registrationDate)
   VALUES ('newuser@example.com', 'newuser', 'New User', '1990-01-01', '2022-01-01');

3. Обновление данных пользователя - update():
   UPDATE user
   SET name = 'Updated User', email = 'updateduser@example.com'
   WHERE user_id = 1;

4. Получение данных о пользователе по id - findById():
   SELECT * FROM user WHERE user_id = 1;

5. Добавление пользователя в друзья - addFriend():
   INSERT INTO friendship (user_id, friend_id)
   VALUES (1, 2);

6. Удаление пользователя из друзей - removeFriend():
   DELETE FROM friendship WHERE user_id = 1 AND friend_id = 2;

7. Получение всех друзей пользователя - findAllFriends():
   SELECT u.*
   FROM user u
   JOIN friendship f ON u.user_id = f.friend_id
   WHERE f.user_id = 1;

8. Получение общих друзей двух пользователей - findCommonFriends():
   SELECT u.*
   FROM user u
   JOIN friendship f1 ON u.user_id = f1.friend_id
   JOIN friendship f2 ON u.user_id = f2.friend_id
   WHERE f1.user_id = 1 AND f2.user_id = 2;
