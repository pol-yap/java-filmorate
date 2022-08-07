package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.controllers.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.controllers.exceptions.BadRequestException;
import ru.yandex.practicum.filmorate.models.User;

import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
@ControllerAdvice
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();
    private int nextId = 1;

    @GetMapping
    public Collection<User> findAll() {
        return users.values();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        int id = getNextId();
        user.setId(id);
        fixName(user);
        users.put(id, user);
        log.debug("Создан новый пользователь {}", user);
        return users.get(id);
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        int id = user.getId();
        if (! users.containsKey(id)) {
            throw new NotFoundException(String.format("Нет пользователя с таким id: %s", id));
        }
        fixName(user);
        users.put(id, user);
        log.debug("Данные пользователя {} изменены {}", id, user);
        return users.get(id);
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

    private void fixName(User user) {
        if (user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.debug("Пользователю {} присвоено имя '{}'", user.getId(), user.getName());
        }
    }

    private int getNextId() {
        return nextId++;
    }
}
