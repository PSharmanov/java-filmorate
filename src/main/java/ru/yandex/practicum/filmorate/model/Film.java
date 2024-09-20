package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.yandex.practicum.filmorate.annotations.ValidReleaseDate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
    private List<Genres> genres = new ArrayList<>();
    private Mpa mpa;
}
