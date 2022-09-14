package ru.yandex.practicum.filmorate.storage.db;

import org.springframework.stereotype.Repository;

@Repository
public class GenreDbStorage extends SimpleDbStorage {
    public GenreDbStorage() {
        super("genres");
    }
}