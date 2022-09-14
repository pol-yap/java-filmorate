package ru.yandex.practicum.filmorate.storage.db;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.storage.FriendStorage;

import java.util.Set;

@Slf4j
@Repository
public class FriendDbStorage extends IntegerLinkDbStorage implements FriendStorage {
    public FriendDbStorage() {
        super("friendship", "friending_id", "friended_id");
    }

    public void addFriend(int friendingId, int friendedId) {
        super.addLink(friendingId, friendedId);
    }

    public void removeFriend(int friendingId, int friendedId) {
        super.removeLink(friendingId, friendedId);
    }

    public Set<Integer> getFriendIds(int userId) {
        return super.findLinked(userId);
    }
}
