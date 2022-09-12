package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.BadRequestException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.List;

@Service
public class FilmService {
    @Autowired
    @Qualifier("filmDBStorage")
    private FilmStorage storage;

    @Autowired
    private UserService userService;

    public Film create(Film film) {
        if (! film.isReleaseDateCorrect()) {
            throw new BadRequestException(String.format(
                    "Неправдоподобная дата релиза %s, до изобретения кинематографа",
                    film.getReleaseDate()));
        }

        return storage.create(film)
                      .orElseThrow(()->new BadRequestException("Не удалось создать новый фильм"));
    }

    public Film update(Film film) {
        throwExceptionIfNoSuchId(film.getId());

        return storage.update(film)
                      .orElseThrow(()->new BadRequestException("Не удалось обновить данные о фильме"));
    }

    public List<Film> findAll() {
        return storage.findAll();
    }

    public Film findById(int id) {
        throwExceptionIfNoSuchId(id);

        return storage.findById(id)
                      .orElseThrow(()->new NotFoundException(id, "фильм"));
    }

    public void addLike(int filmId, int userId) {
        throwExceptionIfNoSuchId(filmId);
        userService.throwExceptionIfNoSuchId(userId);
        storage.addLike(filmId, userId);
    }

    public void removeLike(int filmId, int userId) {
        throwExceptionIfNoSuchId(filmId);
        userService.throwExceptionIfNoSuchId(userId);
        storage.removeLike(filmId, userId);
    }

    public List<Film> getTop(int count) {
        return storage.getTop(count);
    }

    private void throwExceptionIfNoSuchId(int id) {
        if (! storage.isContainsId(id)) {
            throw new NotFoundException(id, "фильм");
        }
    }
}
