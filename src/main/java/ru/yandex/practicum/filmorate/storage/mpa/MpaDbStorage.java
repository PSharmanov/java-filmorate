package ru.yandex.practicum.filmorate.storage.mpa;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.BaseDbStorage;

import java.util.Collection;
import java.util.Optional;

@Repository
public class MpaDbStorage extends BaseDbStorage<Mpa> implements MpaStorage {

    public MpaDbStorage(JdbcTemplate jdbcTemplate, RowMapper<Mpa> mapper) {
        super(jdbcTemplate, mapper, Mpa.class);
    }

    @Override
    public Collection<Mpa> findAll() {
        String sqlQuery = "SELECT * FROM mpa_rating";
        return findMany(sqlQuery);
    }

    @Override
    public Optional<Mpa> findById(long ratingId) {
        String sqlQuery = "SELECT * FROM mpa_rating WHERE mpa_rating_id = ?";
        return findOne(sqlQuery, ratingId);
    }
}
