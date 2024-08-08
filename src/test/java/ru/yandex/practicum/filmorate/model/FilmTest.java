package ru.yandex.practicum.filmorate.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class FilmTest {
    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void validFilm() {
        Film film = new Film();
        film.setId(1L);
        film.setName("Тест Фильм");
        film.setDescription("Описание ТестФильм");
        film.setReleaseDate(LocalDate.of(2024, 1, 1));
        film.setDuration(60L);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertTrue(violations.isEmpty(), "Фильм не прошел проверку на корректность данных");
    }

    @Test
    public void filmWithoutName() {
        Film film = new Film();
        film.setId(1L);
        film.setDescription("Описание ТестФильм");
        film.setReleaseDate(LocalDate.of(2024, 1, 1));
        film.setDuration(60L);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(1, violations.size(), "Должна быть 1 ошибка");
        assertEquals("Введите название фильма", violations.iterator().next().getMessage());
    }

    @Test
    public void longDescription() {
        Film film = new Film();
        film.setId(1L);
        film.setName("Тест Фильм");
        film.setDescription("A".repeat(201));
        film.setReleaseDate(LocalDate.of(2024, 1, 1));
        film.setDuration(60L);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(1, violations.size(), "Должна быть 1 ошибка");
        assertEquals("Максимальная длина описания — 200 символов;", violations.iterator().next().getMessage());
    }

    @Test
    public void releaseDateIsInThePast() {
        Film film = new Film();
        film.setId(1L);
        film.setName("Тест Фильм");
        film.setDescription("Описание ТестФильм");
        film.setReleaseDate(LocalDate.of(1800, 1, 1));
        film.setDuration(60L);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(1, violations.size(), "Должна быть 1 ошибка");
    }

    @Test
    public void negativeDuration() {
        Film film = new Film();
        film.setId(1L);
        film.setName("Тест Фильм");
        film.setDescription("Описание ТестФильм");
        film.setReleaseDate(LocalDate.of(2024, 1, 1));
        film.setDuration(-1L);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(1, violations.size(), "Должна быть 1 ошибка");
        assertEquals("Продолжительность фильма должна быть положительным числом.", violations.iterator().next().getMessage());
    }

    @Test
    public void durationIsZero() {
        Film film = new Film();
        film.setId(1L);
        film.setName("Тест Фильм");
        film.setDescription("Описание ТестФильм");
        film.setReleaseDate(LocalDate.of(2024, 1, 1));
        film.setDuration(0L);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(1, violations.size(), "Должна быть 1 ошибка");
        assertEquals("Продолжительность фильма должна быть положительным числом.", violations.iterator().next().getMessage());
    }

}