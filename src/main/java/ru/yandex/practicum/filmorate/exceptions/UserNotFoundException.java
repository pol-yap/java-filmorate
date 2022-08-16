package ru.yandex.practicum.filmorate.exceptions;

public class UserNotFoundException extends RuntimeException  {
    private final int id;

    public UserNotFoundException(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
