package ru.yandex.practicum.filmorate.storage.db;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.storage.LikeStorage;

import java.util.Set;

@Slf4j
@Repository
public class LikeDbStorage extends IntegerLinkDbStorage implements LikeStorage {
    public LikeDbStorage(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate,"likes", "film_id", "user_id");
    }

    public void addLike(final int filmId, final int userId) {
        super.addLink(filmId, userId);
    }

    public void removeLike(final int filmId, final int userId) {
        super.removeLink(filmId, userId);
    }

    public Set<Integer> findByFilm(int filmId) {
        return super.findLinked(filmId);
    }
}
