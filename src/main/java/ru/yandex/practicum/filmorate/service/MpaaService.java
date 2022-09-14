package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.BadRequestException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.SimpleEntity;
import ru.yandex.practicum.filmorate.storage.SimpleStorage;

import java.util.List;

@Service
public class MpaaService {
    @Autowired
    @Qualifier("mpaaDbStorage")
    private SimpleStorage storage;

    public SimpleEntity create(SimpleEntity mpa) {
        return storage.create(mpa)
                      .orElseThrow(()->new BadRequestException("Не удалось создать новый рейтинг MPAA"));
    }

    public SimpleEntity update(SimpleEntity mpa) {
        throwExceptionIfNoSuchId(mpa.getId());
        return storage.update(mpa)
                      .orElseThrow(()->new BadRequestException("Не удалось обновить рейтинг MPAA"));
    }

    public SimpleEntity findById(int id) {
        throwExceptionIfNoSuchId(id);
        return storage.findById(id)
                      .orElseThrow(()->new NotFoundException(id, "рейтинг MPAA"));
    }

    public List<SimpleEntity> findAll() {
        return storage.findAll();
    }

    protected void throwExceptionIfNoSuchId(int id) {
        if (! storage.isContainsId(id)) {
            throw new NotFoundException(id, "рейтинг MPAA");
        }
    }
}
