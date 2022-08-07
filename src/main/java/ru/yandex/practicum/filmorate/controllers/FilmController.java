package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.controllers.errors.NotFoundException;
import ru.yandex.practicum.filmorate.controllers.errors.WrongDataException;
import ru.yandex.practicum.filmorate.model.Film;

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
    final int MAX_DESCRIPTION_LENGTH = 200;
    final LocalDate CINEMA_BIRTHDAY = LocalDate.of(1895, 12, 28);

    @GetMapping
    public Collection<Film> findAll() {
        return films.values();
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        validateFilm(film);
        int id = getNextId();
        film.setId(id);
        films.put(id, film);
        log.trace("Создан новый фильм {}", film);
        return films.get(id);
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        int id = film.getId();
        if (! films.containsKey(id)) {
            throw new NotFoundException(String.format("Нет фильма с таким id: %d", id));
        }
        validateFilm(film);
        films.put(id, film);
        log.trace("Данные фильма {} изменены {}", id, film);
        return films.get(id);
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String notFound(NotFoundException e) {
        log.warn(e.getMessage());
        return e.getMessage();
    }

    @ExceptionHandler(WrongDataException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String wrongData(WrongDataException e) {
        log.warn(e.getMessage());
        return e.getMessage();
    }

    private void validateFilm(Film film) {
        if (film.getDescription().length() > MAX_DESCRIPTION_LENGTH) {
            throw new WrongDataException(String.format(
                    "Слишком длинное описание '%s', должно быть не длиннее %d",
                    film.getDescription(),
                    MAX_DESCRIPTION_LENGTH));
        }

        if (film.getReleaseDate().isBefore(CINEMA_BIRTHDAY)) {
            throw new WrongDataException(String.format(
                    "Неправдоподобная дата релиза %s, до изобретения кинематографа %s",
                    film.getReleaseDate(),
                    CINEMA_BIRTHDAY));
        }
    }

    private int getNextId() {
        return nextId++;
    }
}
