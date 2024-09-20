package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;

import java.util.Collection;
import java.util.Optional;

@Service
@Slf4j
public class MpaService {
    private final MpaStorage mpaStorage;

    @Autowired
    public MpaService(@Qualifier("mpaDbStorage") MpaStorage mpaStorage) {
        this.mpaStorage = mpaStorage;

    }

    public Mpa findMpaById(long mpaId) {
        log.info("запрос на получение рейтинга по id " + mpaId);
        Optional<Mpa> mpaRating = mpaStorage.findById(mpaId);
        if (mpaRating.isPresent()) {
            log.debug("Рейтинг: " + mpaRating.get().getName());
            return mpaRating.get();
        } else {
            throw new NotFoundException("Не найден рейтинг c id " + mpaId);
        }
    }

    public Collection<Mpa> findAllMpa() {
        log.info("Запрос на получение всех рейтингов.");
        return mpaStorage.findAll();
    }
}


