package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> films = new HashMap<>();

    @Override
    public Collection<Film> findAll() {
        return films.values();
    }

    @Override
    public Optional<Film> findById(long filmId) {
        return films.values().stream()
                .filter(x -> x.getId() == filmId)
                .findFirst();
    }

    @Override
    public Film create(Film film) {
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film update(Film film) {
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public void addLike(long filmId, long userId) {

    }

    @Override
    public void removeLike(long filmId, long userId) {

    }

    @Override
    public List<Film> findPopular(Integer count) {
        return null;
    }
}
