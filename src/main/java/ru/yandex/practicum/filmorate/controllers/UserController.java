package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.controllers.errors.NotFoundException;
import ru.yandex.practicum.filmorate.controllers.errors.WrongDataException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.time.LocalDate;
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
    public User create(@RequestBody User user) {
        validateUser(user);
        int id = getNextId();
        user.setId(id);
        users.put(id, user);
        log.trace("Создан новый пользователь {}", user);
        return users.get(id);
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        int id = user.getId();
        if (! users.containsKey(id)) {
            throw new NotFoundException(String.format("Нет пользователя с таким id: %s", id));
        }
        validateUser(user);
        users.put(id, user);
        log.trace("Данные пользователя {} изменены {}", id, user);
        return users.get(id);
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


    private void validateUser(User user) {
        if (user.getLogin().contains(" ")) {
            throw new WrongDataException(String.format("Login не должен содержать пробелы: '%s'", user.getLogin()));
        }

        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new WrongDataException(String.format("Дата рождения %s пользователя ещё не наступила", user.getBirthday()));
        }

        if (user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.trace("Пользователю присвоено имя '{}'", user.getName());
        }
    }

    private int getNextId() {
        return nextId++;
    }
}
