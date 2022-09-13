package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {
    Optional<User> create(final User user);
    Optional<User> findById(final int id);
    Optional<User> update(final User user);
    void delete(final int id);
    boolean isContainsId(final int id);
    List<User> findAll();
//    void addFriend(final int userId, final int friendId);
//    void removeFriend(final int userId, final int friendId);
    //List<Integer> getFriendsId(final int id);
    List<User> getFriends(final int id);
    List<User> getCommonFriends(final int id, final int otherId);
}
