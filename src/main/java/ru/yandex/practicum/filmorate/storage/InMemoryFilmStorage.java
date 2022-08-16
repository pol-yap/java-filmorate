package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class InMemoryFilmStorage implements FilmStorage{
    private final Map<Integer, Film> films = new HashMap<>();
    private int nextId = 1;

    @Override
    public Film create(final Film film) {
        film.setId(getNextId());
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film update(final int id, final Film film) {
       films.put(id, film);
        return films.get(id);
    }

    @Override
    public Film findById(final int id) {
        return films.get(id);
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
    public Film addLike(int filmId, int userId) {
        films.get(filmId).getLikes().add(userId);
        return films.get(filmId);
    }

    @Override
    public Film removeLike(int filmId, int userId) {
        films.get(filmId).getLikes().remove(userId);
        return films.get(filmId);
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
