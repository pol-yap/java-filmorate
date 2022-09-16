package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.storage.FriendStorage;

import java.util.Set;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FriendService {
    private final FriendStorage storage;

    public void addFriend(final int friendId, final int userId) {
        storage.addFriend(userId, friendId);
    }

    public void removeFriend(final int friendId, final int userId) {
        storage.removeFriend(userId, friendId);
    }

    public Set<Integer> getFriendsId(final int userId) {
        return storage.getFriendIds(userId);
    }
}