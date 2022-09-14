package ru.yandex.practicum.filmorate.storage.db;

import org.springframework.stereotype.Repository;

@Repository
public class MpaaDbStorage extends SimpleDbStorage {
    public MpaaDbStorage() {
        super("mpaa");
    }
}
