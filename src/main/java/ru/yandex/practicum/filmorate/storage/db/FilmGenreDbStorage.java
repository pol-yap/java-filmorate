package ru.yandex.practicum.filmorate.storage.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.FilmGenreStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

@Repository
public class FilmGenreDbStorage implements FilmGenreStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmGenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Set<Genre> findByFilm(int filmId) {
        String sql = "SELECT * FROM genres " +
                "JOIN film_genres fg ON genres.id = fg.genre_id WHERE fg.film_id = ? ORDER BY id";

        return new HashSet<>(jdbcTemplate.query(sql, this::rowToEntity, filmId));
    }

    public void updateFilmGenres(int filmId, Set<Genre> genres) {
        String deleteSql = "DELETE FROM film_genres WHERE film_id = ?";
        jdbcTemplate.update(deleteSql, filmId);

        //формально тут у нас проблема N+1,
        // но поскольку жаеров у фильма всё-таки бывает не много, будем считать это допустимым
        String insertSql = "INSERT INTO film_genres (film_id, genre_id) VALUES (?, ?)";
        genres.forEach(genre -> jdbcTemplate.update(insertSql, filmId, genre.getId()));
    }

    private Genre rowToEntity(final ResultSet resultSet, final int rowNum) throws SQLException {
        return new Genre(
                resultSet.getInt("id"),
                resultSet.getString("name")
        );
    }
}
