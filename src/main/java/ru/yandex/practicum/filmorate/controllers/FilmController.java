package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.controllers.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.controllers.exceptions.BadRequestException;
import ru.yandex.practicum.filmorate.models.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();
    private int nextId = 1;

    @GetMapping
    public Collection<Film> findAll() {
        return films.values();
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        validateReleaseDate(film);
        int id = getNextId();
        film.setId(id);
        films.put(id, film);
        log.debug("Создан новый фильм {}", film);
        return films.get(id);
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        int id = film.getId();
        if (! films.containsKey(id)) {
            throw new NotFoundException(String.format("Нет фильма с таким id: %d", id));
        }
        validateReleaseDate(film);
        films.put(id, film);
        log.debug("Данные фильма {} изменены {}", id, film);
        return films.get(id);
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String notFound(NotFoundException e) {
        log.warn(e.getMessage());
        return e.getMessage();
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String wrongData(BadRequestException e) {
        log.warn(e.getMessage());
        return e.getMessage();
    }

    private void validateReleaseDate(Film film) {
        if (!film.isReleaseDateCorrect()) {
            throw new BadRequestException(String.format(
                    "Неправдоподобная дата релиза %s, до изобретения кинематографа",
                    film.getReleaseDate()));
        }
    }

    private int getNextId() {
        return nextId++;
    }
}
