package ru.yandex.practicum.filmorate.storage.film;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@ComponentScan("ru.yandex.practicum.filmorate")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmDbStorageTest {
    FilmDbStorage filmStorage;

    @Test
    void findAll() {
        Collection<Film> allFilms = filmStorage.findAll();
        assertThat(allFilms).hasSize(3);
    }

    @Test
    void findById_notFound() {
        Optional<Film> film = filmStorage.findById(100);
        assertThat(film).isEmpty();
    }

    @Test
    void findById() {
        Optional<Film> film = filmStorage.findById(1);
        assertThat(film).isPresent();
        assertThat(film.get().getName()).isEqualTo("Фильм 1");
        assertThat(film.get().getDescription()).isEqualTo("Описание фильма 1");
        assertThat(film.get().getReleaseDate()).isEqualTo("2023-03-15");
    }

    @Test
    void create() {

        Film newFilm = new Film();
        newFilm.setName("TestFilm");
        newFilm.setDescription("Тестовый фильм TestFilm");
        newFilm.setReleaseDate(LocalDate.of(2023, 01, 01));
        Mpa mpa = new Mpa();
        mpa.setId(1L);
        newFilm.setMpa(mpa);
        assertThat(filmStorage.create(newFilm))
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", 4L);

    }

    @Test
    void update() {
        Film newFilm = new Film();
        newFilm.setId(3L);
        newFilm.setName("TestFilm");
        newFilm.setDescription("Тестовый фильм TestFilm");
        newFilm.setReleaseDate(LocalDate.of(2024, 11, 11));
        Mpa mpa = new Mpa();
        mpa.setId(1L);
        newFilm.setMpa(mpa);

        assertThat(filmStorage.update(newFilm))
                .isNotNull()
                .hasFieldOrPropertyWithValue("releaseDate", LocalDate.of(2024, 11, 11));
    }

    @Test
    void addLike() {
        List<Film> filmList = filmStorage.findPopular(1);
        filmStorage.addLike(3, 1);
        filmStorage.addLike(3, 2);
        List<Film> newFilmList = filmStorage.findPopular(1);

        assertThat(newFilmList.get(0).getId()).isEqualTo(3);

    }

    @Test
    void removeLike() {
        List<Film> filmList = filmStorage.findPopular(1);
        filmStorage.removeLike(1, 1);
        List<Film> newFilmList = filmStorage.findPopular(1);

        assertThat(newFilmList.get(0).getId()).isEqualTo(2);
    }

    @Test
    void findPopular() {
        List<Film> filmList = filmStorage.findPopular(3);
        assertThat(filmList).isNotEmpty();
        assertThat(filmList).hasSize(3);
    }
}