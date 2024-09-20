package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final FilmService filmService;

    @GetMapping
    public Collection<Film> findAll() {
        log.info("Запрос на получение списка всех фильмов");
        return filmService.findAll();
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        log.info("Запрос на добавление фильма");
        return filmService.create(film);
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film newFilm) {
        log.info("Запрос на обновление информации фильма");
        return filmService.update(newFilm);
    }

    // получение фильма по id
    @PostMapping("/{id}")
    public Film findById(@PathVariable("id") long filmId) {
        return filmService.findById(filmId);
    }

    //установка лайка фильму
    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable("id") long id, @PathVariable("userId") long userId) {
        filmService.addLike(id, userId);
    }

    //удаление лайка фильма
    @DeleteMapping("/{id}/like/{userId}")
    public void removeLike(@PathVariable("id") long id, @PathVariable("userId") long userId) {
        filmService.removeLike(id, userId);
    }

    //получение популярных фильмов
    @GetMapping("/popular")
    public List<Film> findPopular(@RequestParam(defaultValue = "10") final Integer count) {
        return filmService.findPopular(count);
    }

    @GetMapping("/{id}")
    public Film filmById(@PathVariable("id") long filmId) {
        return filmService.findById(filmId);
    }

}
