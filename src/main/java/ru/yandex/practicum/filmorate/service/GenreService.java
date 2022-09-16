package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.BadRequestException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.SimpleEntity;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class GenreService {
    private final GenreStorage storage;

    public Genre create(Genre genre) {
        return storage.create(genre)
                      .orElseThrow(()->new BadRequestException("Не удалось создать новый жанр"));
    }

    public Genre update(Genre genre) {
        throwExceptionIfNoSuchId(genre.getId());
        return storage.update(genre)
                      .orElseThrow(()->new BadRequestException("Не удалось обновить жанр"));
    }

    public Genre findById(int id) {
        throwExceptionIfNoSuchId(id);
        return storage.findById(id)
                      .orElseThrow(()->new NotFoundException(id, "жанр"));
    }

    public List<Genre> findAll() {
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
