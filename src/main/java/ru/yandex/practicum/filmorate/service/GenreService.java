package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genres;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;

import java.util.Collection;
import java.util.Optional;

@Service
@Slf4j
public class GenreService {
    private final GenreStorage genreStorage;

    @Autowired
    public GenreService(@Qualifier("genreDbStorage") GenreStorage genreStorage) {
        this.genreStorage = genreStorage;
    }

    public Genres findGenreById(long genreId) {
        log.info("Запрос на получение жанра по id " + genreId);

        Optional<Genres> genre = genreStorage.findById(genreId);
        if (genre.isPresent()) {
            log.debug("Жанр: " + genre.get().getName());
            return genre.get();
        } else {
            throw new NotFoundException("Не найден жанр с id " + genreId);
        }

    }

    public Collection<Genres> findAllGenre() {
        log.info("Запрос на получение всех жанров");
        return genreStorage.findAll();
    }

    public void addGenreFilm(Genres genre, Long filmId) {
        log.info("Запрос на добавление жанра фильму");
        genreStorage.addGenreFilm(genre, filmId);
    }

}
