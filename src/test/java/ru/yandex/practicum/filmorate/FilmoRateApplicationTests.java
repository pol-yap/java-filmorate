package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpaa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.storage.MPAStorage;
import ru.yandex.practicum.filmorate.storage.db.FilmDBStorage;
import ru.yandex.practicum.filmorate.storage.db.UserDBStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)

class FilmoRateApplicationTests {
    private final UserDBStorage userStorage;
    private final FilmDBStorage filmStorage;
    private final GenreStorage genreStorage;
    private final MPAStorage mpaStorage;

    @Test
    public void testCreateUser() {
        Optional<User> newUser = userStorage.create(User.builder()
                                                        .login("User3")
                                                        .email("mail@server.io")
                                                        .name("User3Name")
                                                        .birthday(LocalDate.of(1825, 12, 26))
                                                        .build());

        assertThat(newUser).isPresent()
                           .hasValueSatisfying((user) -> {
                               assertThat(user).hasFieldOrPropertyWithValue("id", 3);
                               assertThat(user).hasFieldOrPropertyWithValue("name", "User3Name");
                           });
    }

    @Test
    public void testFindUserById() {
        Optional<User> optionalUser = userStorage.findById(1);

        assertThat(optionalUser).isPresent()
                                .hasValueSatisfying((user) -> {
                                    assertThat(user).hasFieldOrPropertyWithValue("id", 1);
                                });
    }

    @Test
    public void testUpdateUser() {
        Optional<User> optionalUser = userStorage.findById(1);
        assertThat(optionalUser).isPresent();

        User storedUser = optionalUser.get();
        storedUser.setLogin("User1+");

        Optional<User> updatedUser = userStorage.update(storedUser);
        assertThat(updatedUser).isPresent()
                               .hasValueSatisfying((user) -> {
                                   assertThat(user).hasFieldOrPropertyWithValue("login", "User1+");
                               });

    }

    @Test
    public void testFriends() {
        userStorage.addFriend(1,2);

        List<User> friends1 = userStorage.getFriends(1);
        assertThat(friends1).hasSize(1);

        List<User> friends2 = userStorage.getFriends(2);
        assertThat(friends2).hasSize(0);

        userStorage.removeFriend(1,2);
        friends1 = userStorage.getFriends(1);
        assertThat(friends1).hasSize(0);
    }

    @Test
    public void testFindAllUsers() {
        List<User> users = userStorage.findAll();

        assertThat(users).hasSize(2);
    }

    @Test
    public void testCreateFilm() {
        Optional<Film> newEntity = filmStorage.create(Film.builder()
                                                        .name("Film3")
                                                        .description("mail@server.io")
                                                        .duration(130)
                                                        .releaseDate(LocalDate.of(1993, 1, 1))
                                                        .mpa(new Mpaa(1, ""))
                                                        .build());

        assertThat(newEntity).isPresent()
                           .hasValueSatisfying((entity) -> {
                               assertThat(entity).hasFieldOrPropertyWithValue("id", 3);
                               assertThat(entity).hasFieldOrPropertyWithValue("name", "Film3");
                           });
    }

    @Test
    public void testFindFilmById() {
        Optional<Film> optionalEntity = filmStorage.findById(1);

        assertThat(optionalEntity).isPresent()
                                .hasValueSatisfying((entity) -> {
                                    assertThat(entity).hasFieldOrPropertyWithValue("id", 1);
                                });
    }

    @Test
    public void testUpdateFilm() {
        Optional<Film> optionalEntity = filmStorage.findById(1);
        assertThat(optionalEntity).isPresent();

        Film storedEntity = optionalEntity.get();
        storedEntity.setName("Film1+");

        Optional<Film> updatedEntity = filmStorage.update(storedEntity);
        assertThat(updatedEntity).isPresent()
                               .hasValueSatisfying((entity) -> {
                                   assertThat(entity).hasFieldOrPropertyWithValue("name", "Film1+");
                               });

    }

    @Test
    public void testFindAllFilms() {
        List<Film> entities = filmStorage.findAll();

        assertThat(entities).hasSize(2);
    }

    @Test
    public void testLikes() {
        filmStorage.addLike(1,1);

        Optional<Film> optionalEntity = filmStorage.findById(1);
        assertThat(optionalEntity).isPresent();
        Film storedEntity = optionalEntity.get();
        assertThat(storedEntity.getLikes()).hasSize(1);

        filmStorage.removeLike(1,1);
        optionalEntity = filmStorage.findById(1);
        assertThat(optionalEntity).isPresent();
        storedEntity = optionalEntity.get();
        assertThat(storedEntity.getLikes()).hasSize(0);
    }

    @Test
    public void testGetTop() {
        filmStorage.addLike(2,1);
        List<Film> top = filmStorage.getTop(1);
        assertThat(top).hasSize(1);
        assertThat(top.get(0).getId()).isEqualTo(2);
    }

    @Test
    public void testCreateGenre() {
        Optional<Genre> newEntity = genreStorage.create(new Genre(0, "новый жанр"));

        assertThat(newEntity).isPresent()
                             .hasValueSatisfying((entity) -> {
                                 assertThat(entity).hasFieldOrPropertyWithValue("id", 7);
                                 assertThat(entity).hasFieldOrPropertyWithValue("name", "новый жанр");
                             });
    }

    @Test
    public void testFindGenreById() {
        Optional<Genre> optionalEntity = genreStorage.findById(1);

        assertThat(optionalEntity).isPresent()
                                  .hasValueSatisfying((entity) -> {
                                      assertThat(entity).hasFieldOrPropertyWithValue("id", 1);
                                  });
    }

    @Test
    public void testUpdateGenre() {
        Optional<Genre> optionalEntity = genreStorage.findById(1);
        assertThat(optionalEntity).isPresent();

        Genre storedEntity = optionalEntity.get();
        storedEntity.setName("теперь такой жанр");

        Optional<Genre> updatedEntity = genreStorage.update(storedEntity);
        assertThat(updatedEntity).isPresent()
                                 .hasValueSatisfying((entity) -> {
                                     assertThat(entity).hasFieldOrPropertyWithValue("name", "теперь такой жанр");
                                 });

    }

    @Test
    public void testFindAllGenres() {
        List<Genre> entities = genreStorage.findAll();

        assertThat(entities).hasSize(7);
    }

    @Test
    public void testCreateMPA() {
        Optional<Mpaa> newEntity = mpaStorage.create(new Mpaa(0, "EX"));

        assertThat(newEntity).isPresent()
                             .hasValueSatisfying((entity) -> {
                                 assertThat(entity).hasFieldOrPropertyWithValue("id", 6);
                                 assertThat(entity).hasFieldOrPropertyWithValue("name", "EX");
                             });
    }

    @Test
    public void testFindMPAById() {
        Optional<Mpaa> optionalEntity = mpaStorage.findById(1);

        assertThat(optionalEntity).isPresent()
                                  .hasValueSatisfying((entity) -> {
                                      assertThat(entity).hasFieldOrPropertyWithValue("id", 1);
                                  });
    }

    @Test
    public void testUpdateMPA() {
        Optional<Mpaa> optionalEntity = mpaStorage.findById(1);
        assertThat(optionalEntity).isPresent();

        Mpaa storedEntity = optionalEntity.get();
        storedEntity.setName("XXX");

        Optional<Mpaa> updatedEntity = mpaStorage.update(storedEntity);
        assertThat(updatedEntity).isPresent()
                                 .hasValueSatisfying((entity) -> {
                                     assertThat(entity).hasFieldOrPropertyWithValue("name", "XXX");
                                 });

    }

    @Test
    public void testFindAllMPA() {
        List<Mpaa> entities = mpaStorage.findAll();

        assertThat(entities).hasSize(5);
    }
}
