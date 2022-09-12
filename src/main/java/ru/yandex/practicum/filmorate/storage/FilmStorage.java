package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmStorage {
    Optional<Film> create(final Film film);
    Optional<Film> findById(final int id);
    Optional<Film> update(final Film film);
    void delete(final int id);
    boolean isContainsId(final int id);
    List<Film> findAll();
    void addLike(final int filmId, final int userId);
    void removeLike(final int filmId, final int userId);
    List<Film> getTop(final int count);
}