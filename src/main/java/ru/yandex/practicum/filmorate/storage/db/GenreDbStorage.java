package ru.yandex.practicum.filmorate.storage.db;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class GenreDbStorage extends SimpleDbStorage<Genre> implements GenreStorage {
    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate, "genres");
    }

    @Override
    Genre rowToEntity(final ResultSet resultSet, final int rowNum) throws SQLException {
        return new Genre(
                resultSet.getInt("id"),
                resultSet.getString("name")
        );
    }
}
