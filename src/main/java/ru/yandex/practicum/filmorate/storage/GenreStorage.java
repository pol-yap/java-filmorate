package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface GenreStorage {
    Optional<Genre> create(final Genre genre);
    Optional<Genre> update(final Genre genre);
    Optional<Genre> findById(final int id);
    boolean isContainsId(final int id);
    List<Genre> findAll();
    Set<Genre> findByFilm(int filmId);
    List<Integer> findIdsByFilm(int filmId);
}
