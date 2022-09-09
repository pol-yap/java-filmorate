package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.BadRequestException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import java.util.List;

@Service
public class UserService {
    @Autowired
    @Qualifier("userDBStorage")
    private UserStorage storage;

    public User create(User user) {
        fixName(user);

        return storage.create(user)
                      .orElseThrow(()->new BadRequestException("Не удалось создать нового пользователя"));
    }

    public User update(int id, User user) {
        throwExceptionIfNoSuchId(id);
        fixName(user);

        return storage.update(id, user)
                      .orElseThrow(()->new BadRequestException("Не удалось обновить данные пользователя"));
    }

    public User findById(int id) {
        return storage.findById(id)
                      .orElseThrow(()->new NotFoundException(id, "пользователь"));
    }

    public List<User> findAll() {
        return storage.findAll();
    }

    public void addFriend(int userId, int friendId) {
        throwExceptionIfNoSuchId(userId);
        throwExceptionIfNoSuchId(friendId);
        storage.addFriend(userId, friendId);
    }

    public void removeFriend(int userId, int friendId) {
        throwExceptionIfNoSuchId(userId);
        throwExceptionIfNoSuchId(friendId);
        storage.removeFriend(userId, friendId);
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
            throw new NotFoundException(id, "пользователь");
        }
    }

    private void fixName(User user) {
        if (user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}
