package ru.yandex.practicum.filmorate.storage.genre;

import ru.yandex.practicum.filmorate.model.Genres;

import java.util.List;
import java.util.Optional;

public interface GenreStorage {
    //получение всех жанров
    List<Genres> findAll();

    //получение жанра по id
    Optional<Genres> findById(long genreId);

    //добавление жанра к фильму
    void addGenreFilm(Genres genre, Long filmId);
}

