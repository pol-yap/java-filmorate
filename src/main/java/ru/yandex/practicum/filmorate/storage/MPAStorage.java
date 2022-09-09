package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.MPA;

import java.util.List;
import java.util.Optional;

public interface MPAStorage {
    Optional<MPA> create(final MPA mpa);
    Optional<MPA> update(final int id, final MPA mpa);
    Optional<MPA> findById(final int id);
    boolean isContainsId(final int id);
    List<MPA> findAll();
}
