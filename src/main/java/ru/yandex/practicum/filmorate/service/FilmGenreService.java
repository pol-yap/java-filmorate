package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.FilmGenreStorage;

import java.util.Set;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmGenreService {
    private final FilmGenreStorage storage;

    public Set<Genre> findByFilm(int filmId) {
        return storage.findByFilm(filmId);
    }

    public void updateFilmGenres(int filmId, Set<Genre> genres) {
        storage.updateFilmGenres(filmId, genres);
    }
}
