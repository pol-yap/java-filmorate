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
    @Qualifier("filmDbStorage")
    private FilmStorage storage;

    @Autowired
    private UserService userService;

    @Autowired
    private GenreService genreService;

    @Autowired
    private LikeService likeService;

    public Film create(Film film) {
        if (! film.isReleaseDateCorrect()) {
            throw new BadRequestException(String.format(
                    "Неправдоподобная дата релиза %s, до изобретения кинематографа",
                    film.getReleaseDate()));
        }

        Film createdFilm =  storage.create(film)
                                   .orElseThrow(()->new BadRequestException("Не удалось создать новый фильм"));
        enrichByLinkedData(createdFilm);

        return createdFilm;
    }

    public Film update(Film film) {
        throwExceptionIfNoSuchId(film.getId());

        Film updatedFilm =  storage.update(film)
                                   .orElseThrow(()->new BadRequestException("Не удалось обновить данные о фильме"));
        enrichByLinkedData(updatedFilm);

        return updatedFilm;
    }

    public void delete(final int id) {
        storage.delete(id);
    }

    public List<Film> findAll() {
        return storage.findAll();
    }

    public Film findById(int id) {
        throwExceptionIfNoSuchId(id);

        Film film = storage.findById(id)
                           .orElseThrow(()->new NotFoundException(id, "фильм"));
        enrichByLinkedData(film);

        return film;
    }

    public void addLike(int filmId, int userId) {
        throwExceptionIfNoSuchId(filmId);
        userService.throwExceptionIfNoSuchId(userId);
        likeService.addLike(filmId, userId);
    }

    public void removeLike(int filmId, int userId) {
        throwExceptionIfNoSuchId(filmId);
        userService.throwExceptionIfNoSuchId(userId);
        likeService.removeLike(filmId, userId);
    }

    public List<Film> getTop(int count) {
        return storage.getTop(count);
    }

    private void throwExceptionIfNoSuchId(int id) {
        if (! storage.isContainsId(id)) {
            throw new NotFoundException(id, "фильм");
        }
    }

    private void enrichByLinkedData(Film film) {
        film.setGenres(genreService.findByFilm(film.getId()));
        film.setLikes(likeService.findByFilm(film.getId()));
    }
}
