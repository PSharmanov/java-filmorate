package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface FilmStorage {
    //получение всех пользователей
    Collection<Film> findAll();

    //получение пользователя по id
    Optional<Film> findById(long filmId);

    //создание пользователя
    Film create(Film film);

    //обновление пользователя
    Film update(Film film);

    //добавление лайка фильму
    void addLike(long filmId, long userId);

    //удаление лайка фильма
    void removeLike(long filmId, long userId);

    //получение популярных фильмов по количеству
    List<Film> findPopular(Integer count);

}
