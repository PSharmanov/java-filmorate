package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genres;
import ru.yandex.practicum.filmorate.storage.BaseDbStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreRowMapper;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
public class FilmDbStorage extends BaseDbStorage<Film> implements FilmStorage {
    private final GenreStorage genreStorage;


    public FilmDbStorage(JdbcTemplate jdbcTemplate, RowMapper<Film> mapper, GenreStorage genreStorage) {
        super(jdbcTemplate, mapper, Film.class);
        this.genreStorage = genreStorage;
    }

    @Override
    public Collection<Film> findAll() {
        String sqlQuery = "SELECT\n" +
                "    f.film_id AS id, \n" +
                "    f.name AS name, \n" +
                "    f.description AS description, \n" +
                "    f.releaseDate AS releaseDate, \n" +
                "    f.duration AS duration, \n" +
                "    mr.mpa_rating_name AS mpa_name, \n" +
                "    mr.mpa_rating_id AS mpa_id, \n" +
                "    mr.description AS mpa_description,\n" +
                "    (\n" +
                "        SELECT GROUP_CONCAT(g.name) \n" +
                "        FROM film_genres fg \n" +
                "        LEFT JOIN genres g ON fg.genre_id = g.genre_id\n" +
                "        WHERE fg.film_id = f.film_id\n" +
                "    ) AS genres\n" +
                "FROM film f\n" +
                "JOIN mpa_rating mr ON f.mpa_rating = mr.mpa_rating_id\n" +
                "ORDER BY f.film_id";
        return findMany(sqlQuery);
    }


    @Override
    public Optional<Film> findById(long filmId) {
        String sqlQuery = "SELECT\n" +
                "    f.film_id AS id, \n" +
                "    f.name AS name, \n" +
                "    f.description AS description, \n" +
                "    f.releaseDate AS releaseDate, \n" +
                "    f.duration AS duration, \n" +
                "    mr.mpa_rating_name AS mpa_name, \n" +
                "    mr.mpa_rating_id AS mpa_id, \n" +
                "    mr.description AS mpa_description,\n" +
                "    (\n" +
                "        SELECT GROUP_CONCAT(g.name) \n" +
                "        FROM film_genres fg \n" +
                "        LEFT JOIN genres g ON fg.genre_id = g.genre_id\n" +
                "        WHERE fg.film_id = f.film_id\n" +
                "    ) AS genres\n" +
                "FROM film f\n" +
                "JOIN mpa_rating mr ON f.mpa_rating = mr.mpa_rating_id\n" +
                "WHERE f.film_id = ?\n" +
                "ORDER BY f.film_id";

        Optional<Film> film = findOne(sqlQuery, filmId);

        String sqlQueryGenre = "SELECT g.*\n" +
                "FROM genres g \n" +
                "JOIN film_genres fg ON g.genre_id = fg.genre_id \n" +
                "WHERE fg.film_id = ?";


        List<Genres> genresList = jdbcTemplate.query(sqlQueryGenre, new GenreRowMapper(), filmId);

        film.ifPresent(value -> value.setGenres(genresList));

        return film;
    }

    @Override
    public Film create(Film film) {
        String sqlQuery = "INSERT INTO film (name, description, releaseDate, duration, mpa_rating) " +
                "VALUES (?, ?, ?, ?, ?)";
        long id = insert(
                sqlQuery,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId()
        );
        film.setId(id);

        film.getGenres().stream()
                .distinct()
                .map(genre -> new Object[]{film.getId(), genre.getId()})
                .forEach(args -> {
                    if (args[1] != null) {
                        insert("INSERT INTO film_genres (film_id, genre_id) VALUES (?, ?)", args);
                    } else {
                        log.warn("Жанр с ID " + film.getId() + " пропущен при добавлении в film_genres");
                    }
                });

        return film;
    }

    @Override
    public Film update(Film film) {
        log.info("Запрос к базе данных на обновление данных фильма id " + film.getId());

        String sqlQuery = "UPDATE film SET name = ?, description = ?, releaseDate = ?, duration = ?, mpa_rating  = ?" +
                "WHERE film_id = ?";
        update(
                sqlQuery,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId()
        );
        log.info("Запрос к базе данных на обновление данных фильма id " + film.getId() + " выполнен.");
        return film;
    }

    @Override
    public void addLike(long filmId, long userId) {
        log.info("Запрос к базе данных на добавление лайка фильму id " + filmId + " пользователем id " + userId);
        String sqlQuery = "INSERT INTO likes (film_id, user_id) VALUES (?, ?)";
        update(sqlQuery, filmId, userId);
    }

    @Override
    public void removeLike(long filmId, long userId) {
        log.info("Запрос к базе данных на удаление лайка фильму id " + filmId + " пользователем id " + userId);
        String sqlQuery = "DELETE FROM likes WHERE film_id = ? AND user_id = ?";
        update(sqlQuery, filmId, userId);
    }

    @Override
    public List<Film> findPopular(Integer count) {
        log.info("Запрос к базе данных на выборку популярных фильмов из " + count);
        String sqlQuery = "SELECT\n" +
                "f.*,\n" +
                "mr.mpa_rating_name AS mpa_name,\n" +
                "mr.mpa_rating_id AS mpa_id,\n" +
                "mr.description AS mpa_description,\n" +
                "COUNT(l.like_id) AS like_count\n" +
                "FROM film AS f\n" +
                "JOIN mpa_rating mr ON f.mpa_rating = mr.mpa_rating_id\n" +
                "JOIN likes AS l ON f.film_id = l.film_id\n" +
                "GROUP BY f.film_id, f.name\n" +
                "ORDER BY like_count DESC\n" +
                "LIMIT ?";

        return findMany(sqlQuery, count);
    }

}
