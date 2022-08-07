package ru.yandex.practicum.filmorate.controllers.errors;

public class WrongDataException extends RuntimeException{
    public WrongDataException(String message) {
        super(message);
    }
}
