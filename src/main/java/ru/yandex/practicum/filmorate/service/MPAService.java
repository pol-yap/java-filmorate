package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.MPAStorage;

import java.util.List;

@Service
public class MPAService {
    @Autowired
    private MPAStorage storage;

    public MPA create(MPA mpa) {
        return storage.create(mpa);
    }

    public MPA update(int id, MPA mpa) {
        throwExceptionIfNoSuchId(id);
        return storage.update(id, mpa);
    }

    public MPA findById(int id) {
        throwExceptionIfNoSuchId(id);
        return storage.findById(id);
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
