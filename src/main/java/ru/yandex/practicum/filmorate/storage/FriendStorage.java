package ru.yandex.practicum.filmorate.storage;

import java.util.Map;
import java.util.Set;

public interface FriendStorage {
    void addFriend(int friendingId, int friendedId);
    void removeFriend(int friendingId, int friendedId);
    Set<Integer> getFriendIds(int userId);
    Map<Integer, Integer> findAll();
}
