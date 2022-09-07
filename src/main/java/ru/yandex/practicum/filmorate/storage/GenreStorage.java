package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface GenreStorage {
    Genre create(final Genre genre);
    Genre update(final int id, final Genre genre);
    Genre findById(final int id);
    boolean isContainsId(int id);
    List<Genre> findAll();
    List<Genre> findByFilm(int filmId);
}
