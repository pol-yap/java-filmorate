package ru.yandex.practicum.filmorate.storage.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.MPAStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class MPADBStorage implements MPAStorage {
    @Autowired
    JdbcTemplate jdbcTemplate;

    public Optional<MPA> create(final MPA mpa) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("mpaa")
                .usingGeneratedKeyColumns("id");
        int id = simpleJdbcInsert.executeAndReturnKey(mpaToMap(mpa)).intValue();
        mpa.setId(id);

        return findById(id);
    }

    public Optional<MPA> update(final int id, final MPA mpa) {
        String sql = "UPDATE mpaa SET name = ? WHERE id = ?";
        jdbcTemplate.update(sql, mpa.getName(), id);

        return findById(id);
    }

    public Optional<MPA> findById(final int id) {
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

    public List<MPA> findAll() {
        String sql = "SELECT * FROM mpaa";

        return jdbcTemplate.query(sql, this::rowToMPA);
    }

    private Map<String, Object> mpaToMap(final MPA mpa) {
        return Map.of("name", mpa.getName());
    }

    private MPA rowToMPA(final ResultSet resultSet, final int rowNum) throws SQLException {
        return new MPA(
                resultSet.getInt("id"),
                resultSet.getString("name")
        );
    }
}

