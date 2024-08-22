package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
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
}
