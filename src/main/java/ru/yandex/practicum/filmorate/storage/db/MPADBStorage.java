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

@Repository
public class MPADBStorage implements MPAStorage {
    @Autowired
    JdbcTemplate jdbcTemplate;

    public MPA create(final MPA mpa) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("MPAA_ratings")
                .usingGeneratedKeyColumns("id");
        int id = simpleJdbcInsert.executeAndReturnKey(mpaToMap(mpa)).intValue();
        mpa.setId(id);

        return mpa;
    }

    public MPA update(final int id, final MPA mpa) {
        String sql = "UPDATE MPAA_ratings SET name = ? WHERE id = ?";
        jdbcTemplate.update(sql, mpa.getName(), id);

        return mpa;
    }

    public MPA findById(final int id) {
        String sql = "SELECT * FROM MPAA_ratings Films WHERE id = ?";

        return jdbcTemplate.queryForObject(sql, this::rowToMPA, id);
    }

    public boolean isContainsId(int id) {
        String sql = "SELECT Count(id) FROM MPAA_ratings WHERE id =?";
        Integer found = jdbcTemplate.queryForObject(sql, Integer.class, id);

        return found == 1;
    }

    public List<MPA> findAll() {
        String sql = "SELECT * FROM MPAA_ratings";

        return jdbcTemplate.query(sql, this::rowToMPA);
    }

    private Map<String, Object> mpaToMap(MPA mpa) {
        return Map.of("name", mpa.getName());
    }

    private MPA rowToMPA(ResultSet resultSet, int rowNum) throws SQLException {
        return new MPA(
                resultSet.getInt("id"),
                resultSet.getString("name")
        );
    }
}

