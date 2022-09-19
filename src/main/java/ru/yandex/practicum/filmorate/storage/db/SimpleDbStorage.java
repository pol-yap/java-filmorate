package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import ru.yandex.practicum.filmorate.model.SimpleEntity;
import ru.yandex.practicum.filmorate.storage.SimpleStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public abstract class SimpleDbStorage<T extends SimpleEntity> implements SimpleStorage<T> {
    private final JdbcTemplate jdbcTemplate;
    private final String tableName;

    public Optional<T> create(final T entity) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName(tableName)
                .usingGeneratedKeyColumns("id");
        int id = simpleJdbcInsert.executeAndReturnKey(entityToMap(entity)).intValue();

        return findById(id);
    }

    public Optional<T> update(final T entity) {
        String sql = String.format("UPDATE %s SET name = ? WHERE id = ?", tableName);
        jdbcTemplate.update(sql, entity.getName(), entity.getId());

        return findById(entity.getId());
    }

    public void delete(final int id) {
        String sql = String.format("DELETE FROM %s WHERE id = ?", tableName);
        jdbcTemplate.update(sql, id);
    }

    public Optional<T> findById(final int id) {
        String sql = String.format("SELECT * FROM %s WHERE id = ?", tableName);
        return jdbcTemplate.query(sql, this::rowToEntity, id).stream().findFirst();
    }

    public boolean isContainsId(final int id) {
        String sql = String.format("SELECT Count(id) FROM %s WHERE id = ?", tableName);
        Integer found = jdbcTemplate.queryForObject(sql, Integer.class, id);

        return found == 1;
    }

    public List<T> findAll() {
        String sql = String.format("SELECT * FROM %s", tableName);

        return new ArrayList<>(jdbcTemplate.query(sql, this::rowToEntity));
    }

    private Map<String, Object> entityToMap(final SimpleEntity entity) {
        return Map.of("name", entity.getName());
    }

    abstract T rowToEntity(final ResultSet resultSet, final int rowNum) throws SQLException;
}
