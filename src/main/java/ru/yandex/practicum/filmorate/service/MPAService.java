package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.BadRequestException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.MPAStorage;

import java.util.List;

@Service
public class MPAService {
    @Autowired
    private MPAStorage storage;

    public MPA create(MPA mpa) {
        return storage.create(mpa)
                      .orElseThrow(()->new BadRequestException("Не удалось создать новый рейтинг MPAA"));
    }

    public MPA update(int id, MPA mpa) {
        throwExceptionIfNoSuchId(id);
        return storage.update(id, mpa)
                      .orElseThrow(()->new BadRequestException("Не удалось обновить рейтинг MPAA"));
    }

    public MPA findById(int id) {
        throwExceptionIfNoSuchId(id);
        return storage.findById(id)
                      .orElseThrow(()->new NotFoundException(id, "рейтинг MPAA"));
    }

    public List<MPA> findAll() {
        return storage.findAll();
    }

    protected void throwExceptionIfNoSuchId(int id) {
        if (! storage.isContainsId(id)) {
            throw new NotFoundException(id, "рейтинг MPAA");
        }
    }
}
