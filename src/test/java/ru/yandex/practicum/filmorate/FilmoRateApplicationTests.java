package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.storage.FriendStorage;
import ru.yandex.practicum.filmorate.storage.LikeStorage;
import ru.yandex.practicum.filmorate.storage.db.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.db.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.db.MpaaDbStorage;
import ru.yandex.practicum.filmorate.storage.db.UserDbStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
//@RequiredArgsConstructor(onConstructor_ = @Autowired)

class FilmoRateApplicationTests {
    @Autowired
    private UserDbStorage userStorage;

    @Autowired
    private FilmDbStorage filmStorage;

    @Autowired
    //@Qualifier("genreDbStorage")
    private GenreDbStorage genreStorage;

    @Autowired
    //@Qualifier("mpaaDbStorage")
    private MpaaDbStorage mpaaStorage;

    @Autowired
    private FriendStorage friendStorage;

    @Autowired
    private LikeStorage likeStorage;

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
        friendStorage.addFriend(1,2);

        Set<Integer> friends1 = friendStorage.getFriendIds(1);
        assertThat(friends1).hasSize(1);

        Set<Integer> friends2 = friendStorage.getFriendIds(2);
        assertThat(friends2).hasSize(0);

        friendStorage.removeFriend(1,2);
        friends1 = friendStorage.getFriendIds(1);
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
    public void testDeleteFilm() {
        filmStorage.delete(1);
        Optional<Film> optionalEntity = filmStorage.findById(1);
        assertThat(optionalEntity).isNotPresent();
    }

    @Test
    public void testFindAllFilms() {
        List<Film> entities = filmStorage.findAll();

        assertThat(entities).hasSize(2);
    }

    @Test
    public void testLikes() {
        likeStorage.addLike(1,1);

        Optional<Film> optionalEntity = filmStorage.findById(1);
        assertThat(optionalEntity).isPresent();
        Film storedEntity = optionalEntity.get();
        storedEntity.setLikes(likeStorage.findByFilm(storedEntity.getId()));
        assertThat(storedEntity.getLikes()).hasSize(1);

        likeStorage.removeLike(1,1);
        optionalEntity = filmStorage.findById(1);
        assertThat(optionalEntity).isPresent();
        storedEntity = optionalEntity.get();
        storedEntity.setLikes(likeStorage.findByFilm(storedEntity.getId()));
        assertThat(storedEntity.getLikes()).hasSize(0);
    }

    @Test
    public void testGetTop() {
        likeStorage.addLike(2,1);
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
        Optional<Mpaa> newEntity = mpaaStorage.create(new Mpaa(0, "EX"));

        assertThat(newEntity).isPresent()
                             .hasValueSatisfying((entity) -> {
                                 assertThat(entity).hasFieldOrPropertyWithValue("id", 6);
                                 assertThat(entity).hasFieldOrPropertyWithValue("name", "EX");
                             });
    }

    @Test
    public void testFindMPAById() {
        Optional<Mpaa> optionalEntity = mpaaStorage.findById(1);

        assertThat(optionalEntity).isPresent()
                                  .hasValueSatisfying((entity) -> {
                                      assertThat(entity).hasFieldOrPropertyWithValue("id", 1);
                                  });
    }

    @Test
    public void testUpdateMPA() {
        Optional<Mpaa> optionalEntity = mpaaStorage.findById(1);
        assertThat(optionalEntity).isPresent();

        Mpaa storedEntity = optionalEntity.get();
        storedEntity.setName("XXX");

        Optional<Mpaa> updatedEntity = mpaaStorage.update(storedEntity);
        assertThat(updatedEntity).isPresent()
                                 .hasValueSatisfying((entity) -> {
                                     assertThat(entity).hasFieldOrPropertyWithValue("name", "XXX");
                                 });

    }

    @Test
    public void testFindAllMPA() {
        List<Mpaa> entities = mpaaStorage.findAll();

        assertThat(entities).hasSize(5);
    }
}
