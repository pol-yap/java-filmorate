package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreStorage {
    Optional<Genre> create(final Genre genre);
    Optional<Genre> update(final int id, final Genre genre);
    Optional<Genre> findById(final int id);
    boolean isContainsId(final int id);
    List<Genre> findAll();
    List<Genre> findByFilm(int filmId);
    List<Integer> findIdsByFilm(int filmId);
}
