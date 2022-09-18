package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.SimpleEntity;

import java.util.List;
import java.util.Optional;

public interface SimpleStorage<T extends SimpleEntity> {
    Optional<T> create(final T entity);
    Optional<T> update(final T entity);
    void delete(final int id);
    Optional<T> findById(final int id);
    boolean isContainsId(final int id);
    List<T> findAll();
}
