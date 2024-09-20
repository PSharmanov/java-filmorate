package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class Mpa {
    private long id;
    private String name;
    @NotNull(message = "Описание рейтинга не должно быть пустым")
    private String description;
}
