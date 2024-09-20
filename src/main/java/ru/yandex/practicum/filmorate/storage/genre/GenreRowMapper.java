package ru.yandex.practicum.filmorate.storage.genre;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genres;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class GenreRowMapper implements RowMapper<Genres> {
    @Override
    public Genres mapRow(ResultSet rs, int rowNum) throws SQLException {
        Genres genre = new Genres();
        genre.setId(rs.getLong("genre_id"));
        genre.setName(rs.getString("name"));
        return genre;
    }
}
