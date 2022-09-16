package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.SimpleEntity;

import java.util.Set;

public interface FilmGenreStorage {
    Set<Genre> findByFilm(int filmId);
    void updateFilmGenres(int filmId, Set<Genre> genres);
}
