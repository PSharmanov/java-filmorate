package ru.yandex.practicum.filmorate.storage.mpa;

import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Collection;
import java.util.Optional;

public interface MpaStorage {
    //получение всех рейтингов
    Collection<Mpa> findAll();

    //получение рейтинга по id
    Optional<Mpa> findById(long ratingId);
}
