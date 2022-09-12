package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.BadRequestException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpaa;
import ru.yandex.practicum.filmorate.storage.MpaaStorage;

import java.util.List;

@Service
public class MpaaService {
    @Autowired
    private MpaaStorage storage;

    public Mpaa create(Mpaa mpa) {
        return storage.create(mpa)
                      .orElseThrow(()->new BadRequestException("Не удалось создать новый рейтинг MPAA"));
    }

    public Mpaa update(Mpaa mpa) {
        throwExceptionIfNoSuchId(mpa.getId());
        return storage.update(mpa)
                      .orElseThrow(()->new BadRequestException("Не удалось обновить рейтинг MPAA"));
    }

    public Mpaa findById(int id) {
        throwExceptionIfNoSuchId(id);
        return storage.findById(id)
                      .orElseThrow(()->new NotFoundException(id, "рейтинг MPAA"));
    }

    public List<Mpaa> findAll() {
        return storage.findAll();
    }

    protected void throwExceptionIfNoSuchId(int id) {
        if (! storage.isContainsId(id)) {
            throw new NotFoundException(id, "рейтинг MPAA");
        }
    }
}
