package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.yandex.practicum.filmorate.annotations.ValidReleaseDate;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Film.
 */
@Data
@EqualsAndHashCode(of = {"id"})
public class Film {
    private Long id;
    @NotBlank(message = "Введите название фильма")
    private String name;
    @Size(max = 200, message = "Максимальная длина описания — 200 символов;")
    private String description;
    @ValidReleaseDate
    private LocalDate releaseDate;
    @Positive(message = "Продолжительность фильма должна быть положительным числом.")
    private Long duration;
    private Set<Long> likes = new HashSet<>();
}
