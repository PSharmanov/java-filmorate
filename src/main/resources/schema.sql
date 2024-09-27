DROP TABLE IF EXISTS mpa_rating CASCADE;
DROP TABLE IF EXISTS genres CASCADE;
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS film CASCADE;
DROP TABLE IF EXISTS status CASCADE;
DROP TABLE IF EXISTS friendship CASCADE;
DROP TABLE IF EXISTS film_genres CASCADE;
DROP TABLE IF EXISTS likes CASCADE;

-- Создание таблицы mpa_rating
CREATE TABLE IF NOT EXISTS mpa_rating (
    mpa_rating_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    mpa_rating_name VARCHAR(10) NOT NULL,
    description VARCHAR(255) NOT NULL);

-- Создание таблицы genres
CREATE TABLE IF NOT EXISTS genres (
    genre_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(100) NOT NULL);

-- Создание таблицы film
CREATE TABLE IF NOT EXISTS film (
    film_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    releaseDate DATE NOT NULL,
    duration INT CHECK (duration > 0),
    mpa_rating BIGINT,
    FOREIGN KEY (mpa_rating) REFERENCES mpa_rating(mpa_rating_id)
);

-- Создание таблицы film_genres
CREATE TABLE IF NOT EXISTS film_genres (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    film_id INT NOT NULL,
    genre_id INT NOT NULL,
    FOREIGN KEY (film_id) REFERENCES film(film_id),
    FOREIGN KEY (genre_id) REFERENCES genres(genre_id),
    UNIQUE (film_id, genre_id) -- Уникальная пара фильм-жанр
);

-- Создание таблицы users
CREATE TABLE IF NOT EXISTS users (
    user_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    email VARCHAR(100) UNIQUE NOT NULL,
    login VARCHAR(50) UNIQUE NOT NULL,
    name VARCHAR(100),
    birthday DATE,
    registrationDate TIMESTAMP
);

-- Создание таблицы status
CREATE TABLE IF NOT EXISTS status (
    status_id VARCHAR(50) PRIMARY KEY,
    description VARCHAR(50) NOT NULL
);

-- Создание таблицы friendship
CREATE TABLE IF NOT EXISTS friendship (
    friendship_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_id INT NOT NULL,
    friend_id INT NOT NULL,
    status_id VARCHAR(50) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (friend_id) REFERENCES users(user_id),
    FOREIGN KEY (status_id) REFERENCES status(status_id),
    UNIQUE (user_id, friend_id) -- Для уникальности пары (пользователь, друг)
);

-- Создание таблицы likes
CREATE TABLE IF NOT EXISTS likes (
    like_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    film_id INT NOT NULL,
    user_id INT NOT NULL,
    FOREIGN KEY (film_id) REFERENCES film(film_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    UNIQUE (film_id, user_id) -- Уникальная пара фильм-пользователь
);

