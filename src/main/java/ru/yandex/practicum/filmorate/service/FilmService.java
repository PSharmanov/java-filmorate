package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genres;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final GenreStorage genreStorage;
    private final MpaStorage mpaStorage;

    @Autowired
    public FilmService(@Qualifier("filmDbStorage") FilmStorage filmStorage,
                       @Qualifier("userDbStorage") UserStorage userStorage,
                       GenreStorage genreStorage,
                       MpaStorage mpaStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.genreStorage = genreStorage;
        this.mpaStorage = mpaStorage;
    }

    public Collection<Film> findAll() {
        return filmStorage.findAll();
    }

    public Film create(Film film) {
        log.info("Запрос на добавление фильма.");

        if (film.getId() != null) {
            film.setId(getNextId());
        }

        film.getGenres().forEach(genre -> {
            Optional<Genres> g = genreStorage.findById(genre.getId());
            if (g.isEmpty()) {
                throw new ValidationException("Не найден жанр");
            }
        });

        Optional<Mpa> mpa = mpaStorage.findById(film.getMpa().getId());
        if (mpa.isEmpty()) {
            throw new ValidationException("Рейтинг не найден");
        }

        film = filmStorage.create(film);
        log.info("Фильм добавлен, id " + film.getId());
        return film;
    }

    public Film update(Film newFilm) {
        log.info("Запрос на обновление информации фильма");

        if (newFilm.getId() == null) {
            log.warn("В запросе на обновление не указан id");
            throw new ValidationException("Id должен быть указан");
        }

        if (filmStorage.findById(newFilm.getId()).isEmpty()) {
            log.warn("Обновление данных фильма не произошло, не найден фильм с id " + newFilm.getId());
            throw new NotFoundException("Фильм с id = " + newFilm.getId() + " не найден");
        }

        filmStorage.update(newFilm);

        log.info("Обновлены данные фильма с id " + newFilm.getId());

        return newFilm;
    }

    // вспомогательный метод для генерации идентификатора нового фильма
    private long getNextId() {
        long currentMaxId = filmStorage.findAll()
                .stream()
                .map(Film::getId)
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    //получение фильма по id
    public Film findById(long filmId) {
        log.info("Запрос на получение фильма с id" + filmId);

        Optional<Film> film = filmStorage.findById(filmId);

        if (film.isEmpty()) {
            log.warn("Фильм с id" + filmId + "не найден");
            throw new NotFoundException("Фильм не найден");
        }

        return film.get();
    }

    //добавление лайка фильма
    public void addLike(long filmId, long userId) {
        log.info("Добавление лайка пользователя с id:" + userId + " к фильму с id: " + filmId);
        Optional<Film> film = filmStorage.findById(filmId);
        Optional<User> user = userStorage.findById(userId);

        if (film.isEmpty()) {
            log.warn("Фильм для добавления лайка с id" + filmId + "не найден");
            throw new NotFoundException("Фильм для добавления лайка не найден");
        }

        if (user.isEmpty()) {
            log.warn("Пользователь добавляющий лайк с id" + userId + "не найден");
            throw new NotFoundException("Пользователь добавляющий лайк не найден");
        }

        Film foundFilm = film.get();
        foundFilm.getLikes().add(userId);
        filmStorage.addLike(filmId, userId);
        log.info("Добавлен лайк к фильму с id " + filmId);
    }

    //удаление лайка фильма
    public void removeLike(long filmId, long userId) {
        log.info("Удаление лайка пользователя с id:" + userId + " к фильму с id: " + filmId);
        Optional<Film> film = filmStorage.findById(filmId);
        Optional<User> user = userStorage.findById(userId);

        if (film.isEmpty()) {
            log.warn("Фильм у которого удаляется лайк не найден. Id фильма: " + filmId);
            throw new NotFoundException("Фильм для удаления лайка не найден");
        }

        if (user.isEmpty()) {
            log.warn("Пользователь лайк которого должен быть удален не найден. Id пользователя: " + userId);
            throw new NotFoundException("Пользователь удаляющий лайк не найден");
        }

        Film foundFilm = film.get();
        foundFilm.getLikes().remove(userId);
        filmStorage.removeLike(filmId, userId);
        log.info("Удален лайк к фильму с id " + filmId);
    }

    //получение популярных фильмов
    public List<Film> findPopular(Integer count) {
        log.info("Запрос на получение популярных фильмов из " + count);
        if (count <= 0) {
            log.warn("Не верное значение count в запросе на получение популярных фильмов. count = " + count);
            throw new ValidationException("Количество фильмов должно быть больше чем 0");
        }

        List<Film> filmSet = filmStorage.findPopular(count);

        log.info("Передан список популярных фильмов.");

        return filmSet;

    }

}
