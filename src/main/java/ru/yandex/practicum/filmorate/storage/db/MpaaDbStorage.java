package ru.yandex.practicum.filmorate.storage.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Mpaa;
import ru.yandex.practicum.filmorate.storage.MpaaStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class MpaaDbStorage implements MpaaStorage {
    @Autowired
    JdbcTemplate jdbcTemplate;

    public Optional<Mpaa> create(final Mpaa mpa) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("mpaa")
                .usingGeneratedKeyColumns("id");
        int id = simpleJdbcInsert.executeAndReturnKey(mpaToMap(mpa)).intValue();
        mpa.setId(id);

        return findById(id);
    }

    public Optional<Mpaa> update(final Mpaa mpa) {
        int id = mpa.getId();
        String sql = "UPDATE mpaa SET name = ? WHERE id = ?";
        jdbcTemplate.update(sql, mpa.getName(), id);

        return findById(id);
    }

    public Optional<Mpaa> findById(final int id) {
        String sql = "SELECT * FROM mpaa Films WHERE id = ?";

        return jdbcTemplate.query(sql, this::rowToMPA, id)
                           .stream()
                           .findFirst();
    }

    public boolean isContainsId(final int id) {
        String sql = "SELECT Count(id) FROM mpaa WHERE id =?";
        Integer found = jdbcTemplate.queryForObject(sql, Integer.class, id);

        return found == 1;
    }

    public List<Mpaa> findAll() {
        String sql = "SELECT * FROM mpaa";

        return jdbcTemplate.query(sql, this::rowToMPA);
    }

    private Map<String, Object> mpaToMap(final Mpaa mpaa) {
        return Map.of("name", mpaa.getName());
    }

    private Mpaa rowToMPA(final ResultSet resultSet, final int rowNum) throws SQLException {
        return new Mpaa(
                resultSet.getInt("id"),
                resultSet.getString("name")
        );
    }
}

