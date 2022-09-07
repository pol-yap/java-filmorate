package ru.yandex.practicum.filmorate.storage.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Repository
public class GenreDBStorage implements GenreStorage {
    @Autowired
    JdbcTemplate jdbcTemplate;

    public Genre create(final Genre genre) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("Genres")
                .usingGeneratedKeyColumns("id");
        int id = simpleJdbcInsert.executeAndReturnKey(genreToMap(genre)).intValue();
        genre.setId(id);

        return genre;
    }

    public Genre update(final int id, final Genre genre) {
        String sql = "UPDATE Genres SET name = ? WHERE id = ?";
        jdbcTemplate.update(sql, genre.getName(), id);

        return genre;
    }

    public Genre findById(final int id) {
        String sql = "SELECT * FROM Genres Films WHERE id = ?";

        return jdbcTemplate.queryForObject(sql, this::rowToGenre, id);
    }

    public boolean isContainsId(int id) {
        String sql = "SELECT Count(id) FROM Genres WHERE id =?";
        Integer found = jdbcTemplate.queryForObject(sql, Integer.class, id);

        return found == 1;
    }

    public List<Genre> findAll() {
        String sql = "SELECT * FROM Genres ORDER BY id";

        return jdbcTemplate.query(sql, this::rowToGenre);
    }

    public List<Genre> findByFilm(int filmId) {
        String sql = "SELECT * FROM Genres " +
                "JOIN Film_genres fg ON Genres.id = fg.genre_id WHERE fg.film_id = ? ORDER BY id";

        return jdbcTemplate.query(sql, this::rowToGenre, filmId);
    }

    private Map<String, Object> genreToMap(Genre genre) {
        return Map.of("name", genre.getName());
    }

    private Genre rowToGenre(ResultSet resultSet, int rowNum) throws SQLException {
        return new Genre(
                resultSet.getInt("id"),
                resultSet.getString("name")
        );
    }
}
