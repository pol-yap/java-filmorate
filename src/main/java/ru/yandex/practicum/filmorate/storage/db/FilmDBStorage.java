package ru.yandex.practicum.filmorate.storage.db;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpaa;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

@Slf4j
@Repository
public class FilmDBStorage implements FilmStorage {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private GenreStorage genreStorage;

    public Optional<Film> create(final Film film) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("films")
                .usingGeneratedKeyColumns("id");
        int id = simpleJdbcInsert.executeAndReturnKey(filmToMap(film)).intValue();
        film.setId(id);
        saveGenres(film);

        return findById(id);
    }

    public Optional<Film> update(final int id, final Film film) {
        String sql = "UPDATE films " +
                "SET name = ?, description = ?, release_date = ?, duration = ?, rating_id = ? " +
                "WHERE id = ?";
        jdbcTemplate.update(sql,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                id);
        saveGenres(film);

        return findById(id);
    }

    public Optional<Film> findById(final int id) {
        String sql = "SELECT *, mpaa.name AS rating_name " +
                "FROM films LEFT JOIN mpaa ON films.rating_id = mpaa.id WHERE films.id = ?";
        return jdbcTemplate.query(sql, this::rowToFilm, id)
                           .stream()
                           .peek(this::loadGenres)
                           .peek(this::loadLikes)
                           .findFirst();
    }

    public boolean isContainsId(final int id) {
        String sql = "SELECT Count(id) FROM films WHERE id =?";
        Integer found = jdbcTemplate.queryForObject(sql, Integer.class, id);

        return found == 1;
    }

    public List<Film> findAll() {
        String sql = "SELECT *, mpaa.name AS rating_name " +
                "FROM films LEFT JOIN mpaa ON films.rating_id = mpaa.id";

        return jdbcTemplate.query(sql, this::rowToFilm)
                           .stream()
                           .peek(this::loadGenres)
                           .peek(this::loadLikes)
                           .collect(Collectors.toList());
    }

    public void addLike(final int filmId, final int userId) {
        String sql = "INSERT INTO likes (film_id, user_id) VALUES (?, ?)";
        jdbcTemplate.update(sql, filmId, userId);
    }

    public void removeLike(final int filmId, final int userId) {
        String sql = "DELETE FROM likes WHERE film_id = ? AND user_id = ?";
        jdbcTemplate.update(sql, filmId, userId);
    }

    public List<Film> getTop(final int count) {
        String sql = "SELECT films.*, mpaa.name AS rating_name, Count(likes.user_id) AS likes_count " +
                "FROM films JOIN mpaa ON films.rating_id = mpaa.id " +
                "LEFT JOIN likes ON films.id = likes.film_id " +
                "GROUP BY films.id " +
                "ORDER BY likes_count DESC " +
                "LIMIT ?";

        return jdbcTemplate.query(sql, this::rowToFilm, count)
                           .stream()
                           .peek(this::loadGenres)
                           .peek(this::loadLikes)
                           .collect(Collectors.toList());
    }

    private Map<String, Object> filmToMap(final Film film) {
        return Map.of(
                "name", film.getName(),
                "description", film.getDescription(),
                "release_date", film.getReleaseDate(),
                "duration", film.getDuration(),
                "rating_id", film.getMpa().getId()
        );
    }

    private Film rowToFilm(final ResultSet resultSet, final int rowNum) throws SQLException {
        return Film.builder()
                   .id(resultSet.getInt("id"))
                   .name(resultSet.getString("name"))
                   .description(resultSet.getString("description"))
                   .releaseDate(resultSet.getDate("release_date").toLocalDate())
                   .duration(resultSet.getInt("duration"))
                   .mpa(new Mpaa(resultSet.getInt("rating_id"),
                                resultSet.getString("rating_name")))
                   .build();
    }

    private void loadGenres(final Film film) {
        film.setGenres(new HashSet<>(genreStorage.findByFilm(film.getId())));
    }

    private void loadLikes(final Film film) {
        String sql = "SELECT user_id FROM likes WHERE film_id = ?";
        film.setLikes(new HashSet<>(jdbcTemplate.queryForList(sql, Integer.class, film.getId())));
    }

    private void saveGenres(final Film film) {
        String sql = "DELETE FROM film_genres WHERE film_id = ?";
        jdbcTemplate.update(sql, film.getId());

        if (film.getGenres() == null) {
            return;
        }

        film.getGenres().forEach((genre)->this.saveGenre(film, genre));
    }

    private void saveGenre(final Film film, final Genre genre) {
        String sql = "INSERT INTO film_genres (film_id, genre_id) VALUES (?, ?)";
        try {
            jdbcTemplate.update(sql, film.getId(), genre.getId());
        } catch (DataAccessException e) {
            log.debug("updateGenresOfFilm: " + e.getMessage());
        }
    }
}
