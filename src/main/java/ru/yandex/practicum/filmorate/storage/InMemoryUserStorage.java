package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();
    private int nextId = 1;

    @Override
    public User create(final User user) {
        user.setId(getNextId());
        users.put(user.getId(), user);

        return user;
    }

    @Override
    public User update(final int id, final User user) {
        users.put(id, user);

        return users.get(id);
    }

    @Override
    public User findById(final int id) {
        return users.get(id);
    }

    @Override
    public List<User> findByIds(List<Integer> ids) {
        return ids.stream()
                .map(this::findById)
                .collect(Collectors.toList());
    }

    @Override
    public List<User> findAll() {
        return List.copyOf(users.values());
    }

    @Override
    public boolean isContainsId(int id) {
        return users.containsKey(id);
    }

    @Override
    public User addFriend(final int userId, final int friendId) {
        users.get(userId).getFriendsId().add(friendId);

        return users.get(userId);
    }

    public User removeFriend(final int userId, final int friendId) {
        users.get(userId).getFriendsId().remove(friendId);

        return users.get(userId) ;
    }

    public List<User> getFriends(final int id) {
        return findByIds(List.copyOf(users.get(id).getFriendsId()));
    }

    public List<User> getCommonFriends(final int id, final int otherId) {
        List<Integer> commonFriendsId = new ArrayList<>(users.get(id).getFriendsId());
        commonFriendsId.retainAll(users.get(otherId).getFriendsId());

        return findByIds(commonFriendsId);
    }

    private int getNextId() {
        return nextId++;
    }

}
