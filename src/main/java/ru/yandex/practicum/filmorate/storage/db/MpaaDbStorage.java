package ru.yandex.practicum.filmorate.storage.db;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Mpaa;
import ru.yandex.practicum.filmorate.storage.MpaaStorage;

import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class MpaaDbStorage extends SimpleDbStorage<Mpaa> implements MpaaStorage {
    public MpaaDbStorage(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate, "mpaa");
    }

    @Override
    Mpaa rowToEntity(final ResultSet resultSet, final int rowNum) throws SQLException {
        return new Mpaa(
                resultSet.getInt("id"),
                resultSet.getString("name")
        );
    }
}