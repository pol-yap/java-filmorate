package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.IntegerLink;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
public abstract class IntegerLinkDbStorage {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final String tableName;
    private final String linkingFieldName;
    private final String linkedFieldName;

    public void addLink(final int linking, final int linked) {
        String sql = String.format("INSERT INTO %s (%s, %s) VALUES (?, ?)",
                tableName,
                linkingFieldName,
                linkedFieldName);
        jdbcTemplate.update(sql, linking, linked);
    }

    public void removeLink(final int linking, final int linked) {
        String sql = String.format("DELETE FROM %s WHERE %s = ? AND %s = ?",
                tableName,
                linkingFieldName,
                linkedFieldName);
        jdbcTemplate.update(sql, linking, linked);
    }

    public void removeAllByLinking(final int linking) {
        String sql = String.format("DELETE FROM %s WHERE %s = ?",
                tableName,
                linkingFieldName);
        jdbcTemplate.update(sql, linking);
    }

    public Set<Integer> findLinking(final int linked) {
        String sql = String.format("SELECT %2$s FROM %1$s WHERE %3$s = ?",
                tableName,
                linkingFieldName,
                linkedFieldName);

        return new HashSet<>(jdbcTemplate.queryForList(sql, Integer.class, linked));
    }

    public Set<Integer> findLinked(final int linking) {
        String sql = String.format("SELECT %3$s FROM %1$s WHERE %2$s = ?",
                tableName,
                linkingFieldName,
                linkedFieldName);

        return new HashSet<>(jdbcTemplate.queryForList(sql, Integer.class, linking));
    }

    public Map<Integer, Integer> findAll() {
        String sql = String.format("SELECT * FROM %s", tableName);

        Map<Integer, Integer> result = new HashMap<>();
        jdbcTemplate.query(sql, this::rowToIntegerLink)
                    .forEach((link)->result.put(link.getLinking(), link.getLinked()));
        return result;
    }

    protected IntegerLink rowToIntegerLink(final ResultSet resultSet, final int rowNum) throws SQLException {
        return new IntegerLink(resultSet.getInt(linkingFieldName), resultSet.getInt(linkedFieldName));
    }
}
