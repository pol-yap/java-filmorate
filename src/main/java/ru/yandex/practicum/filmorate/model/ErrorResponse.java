package ru.yandex.practicum.filmorate.model;

import java.util.ArrayList;
import java.util.List;

public class ErrorResponse {
    private final List<String> errors;

    public ErrorResponse() {
        this.errors = new ArrayList<>();
    }

    public ErrorResponse(String error) {
        this.errors = List.of(error);
    }

    public void addError(String error) {
        errors.add(error);
    }

    public List<String> getError() {
        return errors;
    }
}
