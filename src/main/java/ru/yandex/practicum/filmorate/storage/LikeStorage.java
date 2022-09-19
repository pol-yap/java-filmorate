package ru.yandex.practicum.filmorate.storage;

import java.util.Set;

public interface LikeStorage {
    void addLike(final int filmId, final int userId);
    void removeLike(final int filmId, final int userId);
    Set<Integer> findByFilm(int filmId);
}
