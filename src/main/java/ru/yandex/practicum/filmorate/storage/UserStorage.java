package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    User create(final User user);
    User update(final int id, final User user);
    User findById(final int id);
    List<User> findByIds(final List<Integer> ids);
    boolean isContainsId(final int id);
    List<User> findAll();
    User addFriend(final int userId, final int friendId);
    User removeFriend(final int userId, final int friendId);
    List<User> getFriends(final int id);
    List<User> getCommonFriends(final int id, final int otherId);
}
