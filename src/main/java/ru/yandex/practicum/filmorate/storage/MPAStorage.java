package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.MPA;

import java.util.List;

public interface MPAStorage {
    MPA create(final MPA mpa);
    MPA update(final int id, final MPA mpa);
    MPA findById(final int id);
    boolean isContainsId(int id);
    List<MPA> findAll();
}
