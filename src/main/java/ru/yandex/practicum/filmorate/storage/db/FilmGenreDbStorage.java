package ru.yandex.practicum.filmorate.storage.db;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.ServerErrorException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.FilmGenreStorage;

import javax.sql.RowSet;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class FilmGenreDbStorage implements FilmGenreStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmGenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Set<Genre> findByFilm(Film film) {
        String sql = "SELECT * FROM genres " +
                "JOIN film_genres fg ON genres.id = fg.genre_id WHERE fg.film_id = ? ORDER BY id";

        return new HashSet<>(jdbcTemplate.query(sql, this::rowToGenre, film.getId()));
    }

    public Map<Integer, Set<Genre>> findByFilms(List<Film> films) {
        if (films.size() == 0) {
            return null;
        }

        Map<Integer, Set<Genre>> result = new HashMap<>();
        String sql = "SELECT film_id, genres.id, genres.name FROM genres " +
                "JOIN film_genres fg ON genres.id = fg.genre_id WHERE fg.film_id IN (" +
                Strings.join(films.stream()
                                  .map(Film::getId)
                                  .collect(Collectors.toList()), ',')
                + ") ORDER BY id";

        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql);
        while (rowSet.next()) {
            int film_id = rowSet.getInt(1);
            result.putIfAbsent(film_id, new HashSet<>());
            result.get(film_id).add(new Genre(
                    rowSet.getInt(2),
                    rowSet.getString(3)
            ));
        }

        return result;
    }

    public void updateFilmGenres(Film film, Set<Genre> genres) {
        String deleteSql = "DELETE FROM film_genres WHERE film_id = ?";
        jdbcTemplate.update(deleteSql, film.getId());

        //формально тут у нас проблема N+1,
        // но поскольку жанров у фильма всё-таки бывает не много, будем считать это допустимым
        String insertSql = "INSERT INTO film_genres (film_id, genre_id) VALUES (?, ?)";

        try {
            PreparedStatement preparedStatement = Optional.ofNullable(jdbcTemplate.getDataSource())
                                                          .orElseThrow(()->new ServerErrorException("No DataSource"))
                                                          .getConnection()
                                                          .prepareStatement(insertSql);
            genres.forEach(genre -> prepareInsert(preparedStatement, film.getId(), genre.getId()));
            preparedStatement.executeBatch();
        } catch (SQLException e) {
            throw new ServerErrorException(e.getMessage());
        }
    }

    private void prepareInsert(PreparedStatement preparedStatement, int filmId, int genreId) {
        try {
            preparedStatement.setInt(1, filmId);
            preparedStatement.setInt(2, genreId);
            preparedStatement.addBatch();
        } catch (SQLException e) {
            throw new ServerErrorException(e.getMessage());
        }
    }

    private Genre rowToGenre(final ResultSet resultSet, final int rowNum) throws SQLException {
        return new Genre(
                resultSet.getInt("id"),
                resultSet.getString("name")
        );
    }
}
