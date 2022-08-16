package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import java.util.List;

@Service
public class UserService {
    private final UserStorage storage;

    @Autowired
    public UserService(UserStorage storage) {
        this.storage = storage;
    }

    public User create(User user) {
        fixName(user);
        return storage.create(user);
    }

    public User update(int id, User user) {
        throwExceptionIfNoSuchId(id);
        fixName(user);
        return storage.update(id, user);
    }

    public User findById(int id) {
        throwExceptionIfNoSuchId(id);
        return storage.findById(id);
    }

    public List<User> findAll() {
        return storage.findAll();
    }

    public User addFriend(int userId, int friendId) {
        throwExceptionIfNoSuchId(userId);
        throwExceptionIfNoSuchId(friendId);
        //взаминость!!!
        storage.addFriend(friendId, userId);
        return storage.addFriend(userId, friendId);
    }

    public User removeFriend(int userId, int friendId) {
        throwExceptionIfNoSuchId(userId);
        throwExceptionIfNoSuchId(friendId);
        //взаминость!!!
        storage.removeFriend(friendId, userId);
        return storage.removeFriend(userId, friendId);
    }

    public List<User> getFriends(int id) {
        throwExceptionIfNoSuchId(id);
        return storage.getFriends(id);
    }

    public List<User> getCommonFriends(int userId1, int userId2) {
        throwExceptionIfNoSuchId(userId1);
        throwExceptionIfNoSuchId(userId2);
        return storage.getCommonFriends(userId1, userId2);
    }

    protected void throwExceptionIfNoSuchId(int id) {
        if (! storage.isContainsId(id)) {
            throw new UserNotFoundException(id);
        }
    }

    private void fixName(User user) {
        if (user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}
