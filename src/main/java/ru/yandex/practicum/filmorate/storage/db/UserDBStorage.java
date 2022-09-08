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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Repository
public class UserDBStorage implements UserStorage {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Optional<User> create(final User user) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("Users")
                .usingGeneratedKeyColumns("id");
        int id = simpleJdbcInsert.executeAndReturnKey(userToMap(user)).intValue();

        return findById(id);
    }

    public Optional<User> update(final int id, final User user) {
        String sql = "UPDATE Users SET email = ?, login = ?, name = ?, birthday = ? WHERE id = ?";
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
        String sql = "SELECT * FROM Users WHERE id = ?";
        return jdbcTemplate.query(sql, this::rowToUser, id)
                           .stream()
                           .peek(this::loadFriendsId)
                           .findFirst();
    }

    public boolean isContainsId(final int id) {
        String sql = "SELECT Count(id) FROM Users WHERE id = ?";
        Integer found = jdbcTemplate.queryForObject(sql, Integer.class, id);

        return found == 1;
    }

    public List<User> findAll() {
        String sql = "SELECT * FROM Users";

        return jdbcTemplate.query(sql, this::rowToUser)
                           .stream()
                           .peek(this::loadFriendsId)
                           .collect(Collectors.toList());
    }

    public void addFriend(final int friendId, final int userId) {
        String sql = "INSERT INTO Friendship (friending_id, friended_id) VALUES (?, ?)";
        jdbcTemplate.update(sql, userId, friendId);
    }

    public void removeFriend(final int friendId, final int userId) {
        String sql = "DELETE FROM Friendship WHERE friending_id = ? AND friended_id = ?";
        jdbcTemplate.update(sql, userId, friendId);
    }

    public List<Integer> getFriendsId(final int id) {
        String sql = "SELECT friending_id FROM Friendship WHERE friended_id = ?";

        return jdbcTemplate.queryForList(sql, Integer.class, id);
    }

    public List<User> getFriends(final int id) {
        String sql = "SELECT Users.* FROM Users " +
                "JOIN Friendship ON Users.id = Friendship.friending_id " +
                "WHERE friended_id = ?";

        return jdbcTemplate.query(sql, this::rowToUser, id)
                           .stream()
                           .peek(this::loadFriendsId)
                           .collect(Collectors.toList());
    }

    public List<User> getCommonFriends(final int id1, final int id2) {
        String sql = "SELECT Users.* FROM Friendship f1 " +
                "JOIN Friendship f2 ON (f1.friending_id = f2.friending_id AND f1.friended_id = ? AND f2.friended_id = ?) " +
                "JOIN Users ON Users.id = f1.friending_id";

        return jdbcTemplate.query(sql, this::rowToUser, id1, id2)
                           .stream()
                           .peek(this::loadFriendsId)
                           .collect(Collectors.toList());
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

    private void loadFriendsId(final User user) {
        user.setFriendsId(new HashSet<>(getFriendsId(user.getId())));
    }
}
