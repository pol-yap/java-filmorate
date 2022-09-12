package ru.yandex.practicum.filmorate.storage.memory;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();
    private int nextId = 1;

    @Override
    public Optional<Film> create(final Film film) {
        int id = getNextId();
        film.setId(id);
        films.put(id, film);

        return findById(id);
    }

    @Override
    public Optional<Film> update(final Film film) {
        int id = film.getId();
        films.put(id, film);

        return findById(id);
    }

    @Override
    public Optional<Film> findById(final int id) {
        return Optional.ofNullable(films.get(id));
    }

    @Override
    public void delete(final int id) {
        films.remove(id);
    }

    @Override
    public List<Film> findAll() {
        return List.copyOf(films.values());
    }

    @Override
    public boolean isContainsId(int id) {
        return films.containsKey(id);
    }

    @Override
    public void addLike(int filmId, int userId) {
        films.get(filmId).getLikes().add(userId);
    }

    @Override
    public void removeLike(int filmId, int userId) {
        films.get(filmId).getLikes().remove(userId);
    }

    @Override
    public List<Film> getTop(int count) {
        return films.values().stream()
                .sorted((f1, f2) -> (f2.getLikes().size() - f1.getLikes().size()))
                .limit(count)
                .collect(Collectors.toList());
    }

    private int getNextId() {
        return nextId++;
    }
}
