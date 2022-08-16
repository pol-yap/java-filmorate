package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FilmStorage {
    Film create(final Film film);
    Film update(final int id, final Film film);
    Film findById(final int id);
    boolean isContainsId(int id);
    List<Film> findAll();
    Film addLike(int filmId, int userId);
    Film removeLike(int filmId, int userId);
    List<Film> getTop(int count);
}