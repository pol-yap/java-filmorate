package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.SimpleEntity;

import java.util.List;
import java.util.Optional;

public interface SimpleStorage {
    Optional<SimpleEntity> create(final SimpleEntity entity);
    Optional<SimpleEntity> update(final SimpleEntity entity);
    void delete(final int id);
    Optional<SimpleEntity> findById(final int id);
    boolean isContainsId(final int id);
    List<SimpleEntity> findAll();
}