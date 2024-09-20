package ru.yandex.practicum.filmorate.storage.genre;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.DuplicatedDataException;
import ru.yandex.practicum.filmorate.model.Genres;
import ru.yandex.practicum.filmorate.storage.BaseDbStorage;

import java.util.List;
import java.util.Optional;

@Repository
@Slf4j
public class GenreDbStorage extends BaseDbStorage<Genres> implements GenreStorage {

    public GenreDbStorage(JdbcTemplate jdbcTemplate, RowMapper<Genres> mapper) {
        super(jdbcTemplate, mapper, Genres.class);
    }

    @Override
    public List<Genres> findAll() {
        String sqlQuery = "SELECT * FROM genres";
        return findMany(sqlQuery);
    }

    @Override
    public Optional<Genres> findById(long genreId) {
        String sqlQuery = "SELECT * FROM genres WHERE genre_id = ?";
        return findOne(sqlQuery, genreId);
    }

    @Override
    public void addGenreFilm(Genres genre, Long filmId) {
        log.info("Добавление жанров фильма " + filmId);

        try {
            String sqlQuery = "INSERT INTO film_genres (film_id, genre_id) VALUES (?, ?)";
            insert(sqlQuery,
                    filmId,
                    genre.getId()
            );
        } catch (DataAccessException exception) {
            throw new DuplicatedDataException("Ошибка при добавлении жанров фильму. Жанр дублируется.");
        }

    }


}
