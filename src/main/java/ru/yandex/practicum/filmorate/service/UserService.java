package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.BadRequestException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Service
public class UserService {
    private final UserStorage storage;
    private final FriendStorage friendStorage;

    @Autowired
    public UserService(@Qualifier("userDbStorage") UserStorage storage,
                       FriendStorage friendStorage) {
        this.storage = storage;
        this.friendStorage = friendStorage;
    }

    public User create(User user) {
        fixName(user);

        return storage.create(user)
                      .orElseThrow(()->new BadRequestException("Не удалось создать нового пользователя"));
    }

    public User update(User user) {
        throwExceptionIfNoSuchId(user.getId());
        fixName(user);

        return storage.update(user)
                      .orElseThrow(()->new BadRequestException("Не удалось обновить данные пользователя"));
    }

    public void delete(final int id) {
        storage.delete(id);
    }

    public User findById(int id) {
        User user = storage.findById(id)
                           .orElseThrow(() -> new NotFoundException(id, "пользователь"));
        user.setFriendsId(friendStorage.getFriendIds(user.getId()));

        return user;
    }

    public List<User> findAll() {
        return storage.findAll();
    }

    public void addFriend(int userId, int friendId) {
        throwExceptionIfNoSuchId(userId);
        throwExceptionIfNoSuchId(friendId);
        friendStorage.addFriend(friendId, userId);
    }

    public void removeFriend(int userId, int friendId) {
        throwExceptionIfNoSuchId(userId);
        throwExceptionIfNoSuchId(friendId);
        friendStorage.removeFriend(friendId, userId);
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
