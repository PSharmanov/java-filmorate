package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genres;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.Collection;

@RestController
@RequiredArgsConstructor
@RequestMapping("/genres")
@Slf4j
public class GenreController {
    private final GenreService genreService;

    @GetMapping
    public Collection<Genres> findAllGenre() {
        return genreService.findAllGenre();
    }

    @GetMapping("/{id}")
    public Genres findGenreById(@PathVariable("id") long id) {
        return genreService.findGenreById(id);
    }

}
