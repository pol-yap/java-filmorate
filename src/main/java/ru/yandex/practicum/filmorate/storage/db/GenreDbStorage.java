package ru.yandex.practicum.filmorate.storage.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
public class GenreDbStorage implements GenreStorage {
    @Autowired
    JdbcTemplate jdbcTemplate;

    public Optional<Genre> create(final Genre genre) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("genres")
                .usingGeneratedKeyColumns("id");
        int id = simpleJdbcInsert.executeAndReturnKey(genreToMap(genre)).intValue();

        return findById(id);
    }

    public Optional<Genre> update(final Genre genre) {
        int id = genre.getId();
        String sql = "UPDATE genres SET name = ? WHERE id = ?";
        jdbcTemplate.update(sql, genre.getName(), id);

        return findById(id);
    }

    public Optional<Genre> findById(final int id) {
        String sql = "SELECT * FROM genres Films WHERE id = ?";

        return jdbcTemplate.query(sql, this::rowToGenre, id)
                           .stream()
                           .findFirst();
    }

    public boolean isContainsId(final int id) {
        String sql = "SELECT Count(id) FROM genres WHERE id =?";
        Integer found = jdbcTemplate.queryForObject(sql, Integer.class, id);

        return found == 1;
    }

    public List<Genre> findAll() {
        String sql = "SELECT * FROM genres ORDER BY id";

        return jdbcTemplate.query(sql, this::rowToGenre);
    }

    public Set<Genre> findByFilm(final int filmId) {
        String sql = "SELECT * FROM genres " +
                "JOIN film_genres fg ON genres.id = fg.genre_id WHERE fg.film_id = ? ORDER BY id";

        return new HashSet<>(jdbcTemplate.query(sql, this::rowToGenre, filmId));
    }


    public List<Integer> findIdsByFilm(final int filmId) {
        String sql = "SELECT id FROM genres " +
                "JOIN film_genres fg ON genres.id = fg.genre_id WHERE fg.film_id = ? ORDER BY id";

        return jdbcTemplate.queryForList(sql, Integer.class, filmId);
    }

    private Map<String, Object> genreToMap(final Genre genre) {
        return Map.of("name", genre.getName());
    }

    private Genre rowToGenre(final ResultSet resultSet, final int rowNum) throws SQLException {
        return new Genre(
                resultSet.getInt("id"),
                resultSet.getString("name")
        );
    }
}
