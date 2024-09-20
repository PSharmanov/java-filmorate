package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class FilmRowMapper implements RowMapper<Film> {

    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        Film film = new Film();
        film.setId(rs.getLong("film_id"));
        film.setName(rs.getString("name"));
        film.setDescription(rs.getString("description"));

        Date releaseDate = rs.getDate("releaseDate");
        film.setReleaseDate(releaseDate.toLocalDate());

        film.setDuration(rs.getLong("duration"));

        if (!rs.wasNull()) {
            Mpa mpa = new Mpa();
            mpa.setName(rs.getString("mpa_name"));
            mpa.setId(rs.getLong("mpa_id"));
            mpa.setDescription(rs.getString("mpa_description"));
            film.setMpa(mpa);
        }

        return film;
    }
}


