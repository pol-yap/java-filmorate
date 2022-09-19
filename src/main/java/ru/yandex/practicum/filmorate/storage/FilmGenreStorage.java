package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface FilmGenreStorage {
    Set<Genre> findByFilm(Film film);
    void updateFilmGenres(Film film, Set<Genre> genres);
    Map<Integer, Set<Genre>> findByFilms(List<Film> films);
}
