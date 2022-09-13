package ru.yandex.practicum.filmorate.storage.memory;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();
    private int nextId = 1;

    @Override
    public Optional<User> create(final User user) {
        int id = getNextId();
        user.setId(id);
        users.put(id, user);

        return findById(id);
    }

    @Override
    public Optional<User> update(final User user) {
        int id = user.getId();
        users.put(id, user);

        return findById(id);
    }

    @Override
    public void delete(final int id) {
        users.remove(id);
    }

    @Override
    public Optional<User>  findById(final int id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public List<User> findAll() {
        return List.copyOf(users.values());
    }

    @Override
    public boolean isContainsId(int id) {
        return users.containsKey(id);
    }

    public void addFriend(final int userId, final int friendId) {
        users.get(userId).getFriendsId().add(friendId);
    }

    public void removeFriend(final int userId, final int friendId) {
        users.get(userId).getFriendsId().remove(friendId);
    }

    public List<User> getFriends(final int id) {
        return users.get(id).getFriendsId()
                    .stream()
                    .map(this::findById)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toList());
    }

    public List<User> getCommonFriends(final int id, final int otherId) {
        List<Integer> commonFriendsId = new ArrayList<>(users.get(id).getFriendsId());
        commonFriendsId.retainAll(users.get(otherId).getFriendsId());

        return commonFriendsId.stream()
                              .map(this::findById)
                              .filter(Optional::isPresent)
                              .map(Optional::get)
                              .collect(Collectors.toList());
    }

    private int getNextId() {
        return nextId++;
    }
}
