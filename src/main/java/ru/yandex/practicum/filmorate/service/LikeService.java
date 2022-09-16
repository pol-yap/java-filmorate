package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.storage.LikeStorage;

import java.util.Set;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class LikeService {
    private final LikeStorage storage;

    public void addLike(int userId, int filmId) {
        storage.addLike(userId, filmId);
    }

    public void removeLike(int userId, int filmId) {
        storage.removeLike(userId, filmId);
    }

    public Set<Integer> findByFilm(int filmId) {
        return storage.findByFilm(filmId);
    }
}
