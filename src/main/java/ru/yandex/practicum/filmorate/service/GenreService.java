package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.BadRequestException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.SimpleEntity;
import ru.yandex.practicum.filmorate.storage.SimpleStorage;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GenreService {
    @Autowired
    @Qualifier("genreDbStorage")
    private SimpleStorage storage;

    public SimpleEntity create(SimpleEntity genre) {
        return storage.create(genre)
                      .orElseThrow(()->new BadRequestException("Не удалось создать новый жанр"));
    }

    public SimpleEntity update(SimpleEntity genre) {
        throwExceptionIfNoSuchId(genre.getId());
        return storage.update(genre)
                      .orElseThrow(()->new BadRequestException("Не удалось обновить жанр"));
    }

    public SimpleEntity findById(int id) {
        throwExceptionIfNoSuchId(id);
        return storage.findById(id)
                      .orElseThrow(()->new NotFoundException(id, "жанр"));
    }

    public List<SimpleEntity> findAll() {
        return storage.findAll()
                      .stream()
                      .sorted(Comparator.comparingInt(SimpleEntity::getId))
                      .collect(Collectors.toList());
    }


    protected void throwExceptionIfNoSuchId(int id) {
        if (! storage.isContainsId(id)) {
            throw new NotFoundException(id, "жанр");
        }
    }
}
