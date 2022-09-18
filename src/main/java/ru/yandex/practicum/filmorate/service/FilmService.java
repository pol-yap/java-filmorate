package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.BadRequestException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.FilmGenreStorage;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Service
public class FilmService {
    private final FilmStorage storage;
    private final UserService userService;
    private final FilmGenreStorage filmGenreStorage;
    private final LikeService likeService;

    @Autowired
    public FilmService(@Qualifier("filmDbStorage") FilmStorage storage,
                       UserService userService,
                       FilmGenreStorage filmGenreStorage,
                       LikeService likeService) {

        this.storage = storage;
        this.userService = userService;
        this.filmGenreStorage = filmGenreStorage;
        this.likeService = likeService;
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
        optionalGenres.ifPresent(genres -> filmGenreStorage.updateFilmGenres(createdFilm, genres));
        enrichByLikesAndGenres(createdFilm);

        return createdFilm;
    }

    public Film update(Film film) {
        throwExceptionIfNoSuchId(film.getId());

        Optional<Set<Genre>> optionalGenres = Optional.ofNullable(film.getGenres());

        Film updatedFilm =  storage.update(film)
                                   .orElseThrow(()->new BadRequestException("Не удалось обновить данные о фильме"));
        optionalGenres.ifPresent(genres -> filmGenreStorage.updateFilmGenres(film, genres));
        enrichByLikesAndGenres(updatedFilm);

        return updatedFilm;
    }

    public void delete(final int id) {
        storage.delete(id);
    }

    public List<Film> findAll() {
        List<Film> allFilms = storage.findAll();
        enrichByLikesAndGenres(allFilms);
        return allFilms;
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
        List<Film> top = storage.getTop(count);
        enrichByLikesAndGenres(top);
        return top;
    }

    private void throwExceptionIfNoSuchId(int id) {
        if (! storage.isContainsId(id)) {
            throw new NotFoundException(id, "фильм");
        }
    }

    private void enrichByLikesAndGenres(Film film) {
        film.setGenres(filmGenreStorage.findByFilm(film));
        film.setLikes(likeService.findByFilm(film.getId()));
    }

    private void enrichByLikesAndGenres(List<Film> films) {
        Map<Integer, Set<Genre>> genreData = filmGenreStorage.findByFilms(films);
        films.forEach(film -> film.setGenres(genreData.get(film.getId())));
    }
}
