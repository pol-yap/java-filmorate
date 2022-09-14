package ru.yandex.practicum.filmorate.storage.db;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Slf4j
@Repository
public class UserDbStorage implements UserStorage {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Optional<User> create(final User user) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");
        int id = simpleJdbcInsert.executeAndReturnKey(userToMap(user)).intValue();

        return findById(id);
    }

    public Optional<User> update(final User user) {
        int id = user.getId();
        String sql = "UPDATE users SET email = ?, login = ?, name = ?, birthday = ? WHERE id = ?";
        jdbcTemplate.update(
                sql,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                id);

        return findById(id);
    }

    public Optional<User> findById(final int id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        return jdbcTemplate.query(sql, this::rowToUser, id)
                                                  .stream()
                                                  .findFirst();
    }

    public void delete(final int id) {
        String sql = "DElETE FROM users WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public boolean isContainsId(final int id) {
        String sql = "SELECT Count(id) FROM users WHERE id = ?";
        Integer found = jdbcTemplate.queryForObject(sql, Integer.class, id);

        return found == 1;
    }

    public List<User> findAll() {
        String sql = "SELECT * FROM users";

        return new ArrayList<>(jdbcTemplate.query(sql, this::rowToUser));
    }

    public List<User> getFriends(final int id) {
        String sql = "SELECT users.* FROM users " +
                "JOIN friendship ON users.id = friendship.friending_id " +
                "WHERE friended_id = ?";

        return jdbcTemplate.query(sql, this::rowToUser, id);
    }

    public List<User> getCommonFriends(final int id1, final int id2) {
        String sql = "SELECT users.* FROM friendship f1 " +
                "JOIN friendship f2 ON (f1.friending_id = f2.friending_id AND f1.friended_id = ? AND f2.friended_id = ?) " +
                "JOIN users ON users.id = f1.friending_id";

        return jdbcTemplate.query(sql, this::rowToUser, id1, id2);
    }

    private Map<String, Object> userToMap(final User user) {
        return Map.of(
                "email", user.getEmail(),
                "login", user.getLogin(),
                "name", user.getName(),
                "birthday", user.getBirthday());
    }

    private User rowToUser(final ResultSet resultSet, final int rowNum) throws SQLException {
        return User.builder()
                   .id(resultSet.getInt("id"))
                   .email(resultSet.getString("email"))
                   .login(resultSet.getString("login"))
                   .name(resultSet.getString("name"))
                   .birthday(resultSet.getDate("birthday").toLocalDate())
                   .build();
    }
}
