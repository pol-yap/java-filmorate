package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.BadRequestException;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.List;

@Service
public class FilmService {
    private final FilmStorage storage;
    private final UserService userService;

    @Autowired
    public FilmService(FilmStorage storage, UserService userService) {
        this.storage = storage;
        this.userService = userService;
    }

    public Film create(Film film) {
        if (! film.isReleaseDateCorrect()) {
            throw new BadRequestException(String.format(
                    "Неправдоподобная дата релиза %s, до изобретения кинематографа",
                    film.getReleaseDate()));
        }

        return storage.create(film);
    }

    public Film update(int id, Film film) {
        throwExceptionIfNoSuchId(id);
        return storage.update(id, film);
    }

    public List<Film> findAll() {
        return storage.findAll();
    }

    public Film findById(int id) {
        throwExceptionIfNoSuchId(id);
        return storage.findById(id);
    }

    public Film addLike(int filmId, int userId) {
        throwExceptionIfNoSuchId(filmId);
        userService.throwExceptionIfNoSuchId(userId);
        return storage.addLike(filmId, userId);
    }

    public Film removeLike(int filmId, int userId) {
        throwExceptionIfNoSuchId(filmId);
        userService.throwExceptionIfNoSuchId(userId);
        return storage.removeLike(filmId, userId);
    }

    public List<Film> getTop(int count) {
        return storage.getTop(count);
    }

    private void throwExceptionIfNoSuchId(int id) {
        if (! storage.isContainsId(id)) {
            throw new FilmNotFoundException(id);
        }
    }
}
