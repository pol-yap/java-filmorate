package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Mpaa;

import java.util.List;
import java.util.Optional;

public interface MPAStorage {
    Optional<Mpaa> create(final Mpaa mpa);
    Optional<Mpaa> update(final int id, final Mpaa mpa);
    Optional<Mpaa> findById(final int id);
    boolean isContainsId(final int id);
    List<Mpaa> findAll();
}
