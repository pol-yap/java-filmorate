package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.BadRequestException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class FilmService {
    private final FilmStorage storage;
    private final UserService userService;
    private final GenreService genreService;
    private final LikeService likeService;
    private final FilmGenreService filmGenreService;

    @Autowired
    public FilmService(@Qualifier("filmDbStorage") FilmStorage storage,
                       UserService userService,
                       GenreService genreService,
                       LikeService likeService,
                       FilmGenreService filmGenreService) {

        this.storage = storage;
        this.userService = userService;
        this.genreService = genreService;
        this.likeService = likeService;
        this.filmGenreService = filmGenreService;
    }

    public Film create(Film film) {
        if (! film.isReleaseDateCorrect()) {
            throw new BadRequestException(String.format(
                    "Неправдоподобная дата релиза %s, до изобретения кинематографа",
                    film.getReleaseDate()));
        }

        Optional<Set<Genre>> optionalGenres = Optional.ofNullable(film.getGenres()) ;

        Film createdFilm =  storage.create(film)
                                   .orElseThrow(()->new BadRequestException("Не удалось создать новый фильм"));
        optionalGenres.ifPresent(genres -> filmGenreService.updateFilmGenres(createdFilm.getId(), genres));
        enrichByLikesAndGenres(createdFilm);

        return createdFilm;
    }

    public Film update(Film film) {
        int id = film.getId();
        throwExceptionIfNoSuchId(id);

        Optional<Set<Genre>> optionalGenres = Optional.ofNullable(film.getGenres());

        Film updatedFilm =  storage.update(film)
                                   .orElseThrow(()->new BadRequestException("Не удалось обновить данные о фильме"));
        optionalGenres.ifPresent(genres -> filmGenreService.updateFilmGenres(id, genres));
        enrichByLikesAndGenres(updatedFilm);

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
        enrichByLikesAndGenres(film);

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

    private void enrichByLikesAndGenres(Film film) {
        film.setGenres(filmGenreService.findByFilm(film.getId()));
        film.setLikes(likeService.findByFilm(film.getId()));
    }
}
