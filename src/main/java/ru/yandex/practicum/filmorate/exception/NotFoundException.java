package ru.yandex.practicum.filmorate.exception;

public class NotFoundException extends RuntimeException{
    private final int id;
    private final String entity;

    public NotFoundException(int id, String entity)
    {
        this.id = id;
        this.entity = entity;
    }

    public int getId() {
        return id;
    }

    public String getEntity() {
        return entity;
    }
}
