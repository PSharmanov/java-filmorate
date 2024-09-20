package ru.yandex.practicum.filmorate.storage.mpa;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class MpaRowMapper implements RowMapper<Mpa> {
    @Override
    public Mpa mapRow(ResultSet rs, int rowNum) throws SQLException {
        Mpa mpaRating = new Mpa();
        mpaRating.setId(rs.getLong("mpa_rating_id"));
        mpaRating.setName(rs.getString("mpa_rating_name"));
        mpaRating.setDescription(rs.getString("description"));
        return mpaRating;
    }
}
