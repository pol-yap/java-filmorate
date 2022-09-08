package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.BadRequestException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.List;

@Service
public class GenreService {
    @Autowired
    private GenreStorage storage;

    public Genre create(Genre genre) {
        return storage.create(genre)
                      .orElseThrow(()->new BadRequestException("Не удалось создать новый жанр"));
    }

    public Genre update(int id, Genre genre) {
        throwExceptionIfNoSuchId(id);
        return storage.update(id, genre)
                      .orElseThrow(()->new BadRequestException("Не удалось обновить жанр"));
    }

    public Genre findById(int id) {
        throwExceptionIfNoSuchId(id);
        return storage.findById(id)
                      .orElseThrow(()->new NotFoundException(id, "жанр"));
    }

    public List<Genre> findAll() {
        return storage.findAll();
    }

    protected void throwExceptionIfNoSuchId(int id) {
        if (! storage.isContainsId(id)) {
            throw new NotFoundException(id, "жанр");
        }
    }
}
