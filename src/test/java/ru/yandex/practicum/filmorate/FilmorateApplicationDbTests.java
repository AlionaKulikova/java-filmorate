package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.dal.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.*;

@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:schema.sql")
@SpringBootTest
class FilmorateApplicationDbTests {
    private final UserDbStorage userDBStorage;
    private final FilmDbStorage filmDBStorage;
    private final LikeDbStorage likeDBStorage;
    private final GenreDBStorage genreDBStorage;
    private final MpaDbStorage mpaDBStorage;

    @Test
    public void getAllUsersTest() {
        User user = User.builder()
                .id(1L)
                .name("TestName")
                .email("testMail@mail.ru")
                .login("TestLogin")
                .birthday(LocalDate.of(2001, 7, 5))
                .friends(new HashSet<>())
                .build();
        User userTwo = User.builder()
                .id(2L)
                .name("TestNameTwo")
                .email("testMailTwo@mail.ru")
                .login("TestLoginTwo")
                .birthday(LocalDate.of(2002, 8, 7))
                .friends(new HashSet<>())
                .build();
        userDBStorage.saveUser(user);
        userDBStorage.saveUser(userTwo);
        Assertions.assertEquals(2, userDBStorage.getAllUsers().size(), "Неверное количество добавленных пользователей.");
    }

    @Test
    public void getUserByIdTest() {
        User user = User.builder()
                .id(1L)
                .name("TestName")
                .email("testMail@mail.ru")
                .login("TestLogin")
                .birthday(LocalDate.of(2001, 7, 5))
                .friends(new HashSet<>())
                .build();
        userDBStorage.saveUser(user);
        Assertions.assertEquals(user, userDBStorage.getUserById(1L), "Не получили пользователя по id");
    }

    @Test
    public void saveUserTest() {
        User user = User.builder()
                .id(1L)
                .name("TestName")
                .email("testMail@mail.ru")
                .login("TestLogin")
                .birthday(LocalDate.of(2001, 7, 5))
                .friends(new HashSet<>())
                .build();
        userDBStorage.saveUser(user);
        Assertions.assertEquals(user, userDBStorage.getUserById(1L), "Пользователи не совпадают");
    }

    @Test
    public void updateUserTest() {
        User user = User.builder()
                .id(1L)
                .name("TestName")
                .email("testMail@mail.ru")
                .login("TestLogin")
                .birthday(LocalDate.of(2001, 7, 5))
                .friends(new HashSet<>())
                .build();
        userDBStorage.saveUser(user);
        user.setEmail("newMail@mail.ru");
        userDBStorage.updateUser(user);
        Assertions.assertEquals(user, userDBStorage.getUserById(1L), "Не обновился пользователь");
    }

    @Test
    public void getAllFriendByUserIdTest() {
        User user = User.builder()
                .id(1L)
                .name("TestName")
                .email("testMail@mail.ru")
                .login("TestLogin")
                .birthday(LocalDate.of(2001, 7, 5))
                .friends(new HashSet<>())
                .build();
        User friend = User.builder()
                .id(2L)
                .name("TestNameFriend")
                .email("testMailFriend@mail.ru")
                .login("TestLoginFriend")
                .birthday(LocalDate.of(1995, 8, 1))
                .friends(new HashSet<>())
                .build();

        userDBStorage.saveUser(user);
        userDBStorage.saveUser(friend);
        userDBStorage.userAddFriend(1L, 2L);
        User[] expected = {friend};
        Assertions.assertArrayEquals(expected, userDBStorage.getAllFriendByUserId(1L).toArray(), "Не получили" +
                " всех друзей пользователя.");
    }

    @Test
    public void userAddFriendTest() {
        User user = User.builder()
                .id(1L)
                .name("TestName")
                .email("testMail@mail.ru")
                .login("TestLogin")
                .birthday(LocalDate.of(2001, 7, 5))
                .friends(new HashSet<>())
                .build();
        User friend = User.builder()
                .id(1L)
                .name("TestNameFriend")
                .email("testMailFriend@mail.ru")
                .login("TestLoginFriend")
                .birthday(LocalDate.of(1995, 8, 1))
                .friends(new HashSet<>())
                .build();
        userDBStorage.saveUser(user);
        userDBStorage.saveUser(friend);
        userDBStorage.userAddFriend(1L, 2L);
        User[] arrayFriends = new User[]{friend};
        Assertions.assertArrayEquals(arrayFriends, userDBStorage.getAllFriendByUserId(1L).toArray(), "К" +
                " пользователю друг не добавился");
    }

    @Test
    public void userDeleteFriendTest() {
        User user = User.builder()
                .id(1L)
                .name("TestName")
                .email("testMail@mail.ru")
                .login("TestLogin")
                .birthday(LocalDate.of(2001, 7, 5))
                .friends(new HashSet<>())
                .build();
        User friend = User.builder()
                .id(2L)
                .name("TestNameFriend")
                .email("testMailFriend@mail.ru")
                .login("TestLoginFriend")
                .birthday(LocalDate.of(1995, 8, 1))
                .friends(new HashSet<>())
                .build();
        userDBStorage.saveUser(user);
        userDBStorage.saveUser(friend);
        userDBStorage.userAddFriend(1L, 2L);
        User[] arrayFriends = {friend};
        Assertions.assertArrayEquals(arrayFriends, userDBStorage.getAllFriendByUserId(1L).toArray());
        userDBStorage.userDeleteFriend(1L, 2L);
        User[] expectedAfterDelete = {};
        Assertions.assertArrayEquals(expectedAfterDelete, userDBStorage.getAllFriendByUserId(1L).toArray(),
                "Ожидалось удаление friend у конкретного User");
    }

    @Test
    public void getAllFilmsTest() {
        Film film = Film.builder()
                .id(1L)
                .name("TestName")
                .description("TestDescription")
                .releaseDate(LocalDate.of(2005, 7, 1))
                .duration(100)
                .genres(Set.of(new Genre(3L, "Мультфильм")))
                .likes(new HashSet<>())
                .mpa(new Mpa(4L, "PG-13"))
                .build();
        Film filmTwo = Film.builder()
                .id(2L)
                .name("TestNameTwo")
                .description("TestDescriptionTwo")
                .releaseDate(LocalDate.of(2006, 8, 10))
                .duration(120)
                .genres(Set.of(new Genre(1L, "Комедия")))
                .likes(new HashSet<>())
                .mpa(new Mpa(4L, "PG-13"))
                .build();
        filmDBStorage.saveFilm(film);
        filmDBStorage.saveFilm(filmTwo);
        Assertions.assertEquals(2, filmDBStorage.getAllFilms().size(), "Размер списка фильмов " +
                "не соответствует количеству добавленных фильмов.");
    }

    @Test
    public void getFilmByIdTest() {
        Film film = Film.builder()
                .id(1L)
                .name("TestName")
                .description("TestDescription")
                .releaseDate(LocalDate.of(2005, 7, 1))
                .duration(100)
                .genres(Set.of(new Genre(3L, "Мультфильм")))
                .likes(new HashSet<>())
                .mpa(new Mpa(4L, "PG-13"))
                .build();
        filmDBStorage.saveFilm(film);
        Assertions.assertEquals(film, filmDBStorage.getFilmById(1L), "Не нашли фильм по id");
    }

    @Test
    public void saveFilmTest() {
        Film film = Film.builder()
                .id(1L)
                .name("TestName")
                .description("TestDescription")
                .releaseDate(LocalDate.of(2005, 7, 1))
                .duration(100)
                .genres(Set.of(new Genre(3L, "Мультфильм")))
                .likes(new HashSet<>())
                .mpa(new Mpa(4L, "PG-13"))
                .build();
        filmDBStorage.saveFilm(film);
        Assertions.assertEquals(film, filmDBStorage.getFilmById(1L), "Не получилось создать фильм.");
    }

    @Test
    public void updateFilmTest() {
        Film film = Film.builder()
                .id(1L)
                .name("TestName")
                .description("TestDescription")
                .releaseDate(LocalDate.of(2005, 7, 1))
                .duration(100)
                .genres(Set.of(new Genre(3L, "Мультфильм")))
                .likes(new HashSet<>())
                .mpa(new Mpa(4L, "PG-13"))
                .build();
        filmDBStorage.saveFilm(film);
        film.setName("newTestName");
        filmDBStorage.updateFilm(film);
        Assertions.assertEquals(film, filmDBStorage.getFilmById(1L), "Не получилось обновить фильм.");
    }

    @Test
    public void getLikesByFilmIdTest() {
        Film film = Film.builder()
                .id(1L)
                .name("TestName")
                .description("TestDescription")
                .releaseDate(LocalDate.of(2005, 7, 1))
                .duration(100)
                .genres(Set.of(new Genre(3L, "Мультфильм")))
                .likes(new HashSet<>())
                .mpa(new Mpa(4L, "PG-13"))
                .build();
        filmDBStorage.saveFilm(film);

        User user = User.builder()
                .id(1L)
                .name("TestName")
                .email("testMail@mail.ru")
                .login("TestLogin")
                .birthday(LocalDate.of(2001, 7, 5))
                .friends(new HashSet<>())
                .build();
        userDBStorage.saveUser(user);
        likeDBStorage.addLike(1L, 1L);

        User userTwo = User.builder()
                .id(2L)
                .name("TestNameTwo")
                .email("testMailTwo@mail.ru")
                .login("TestLoginTwo")
                .birthday(LocalDate.of(2000, 4, 1))
                .friends(new HashSet<>())
                .build();
        userDBStorage.saveUser(userTwo);
        likeDBStorage.addLike(1L, 2L);

        Long[] allArrLikes = {1L, 2L};
        Assertions.assertArrayEquals(allArrLikes, filmDBStorage.getFilmById(1L).getLikes().toArray(),
                "Неверное количество лайков");
    }

    @Test
    public void addLikeByFilmIdTest() {
        Film film = Film.builder()
                .id(1L)
                .name("TestName")
                .description("TestDescription")
                .releaseDate(LocalDate.of(2005, 7, 1))
                .duration(100)
                .genres(Set.of(new Genre(3L, "Мультфильм")))
                .likes(new HashSet<>())
                .mpa(new Mpa(4L, "PG-13"))
                .build();
        filmDBStorage.saveFilm(film);

        User user = User.builder()
                .id(1L)
                .name("TestName")
                .email("testMail@mail.ru")
                .login("TestLogin")
                .birthday(LocalDate.of(2001, 7, 5))
                .friends(new HashSet<>())
                .build();
        userDBStorage.saveUser(user);
        likeDBStorage.addLike(1L, 1L);
        Long[] arrayLikes = {1L};
        Assertions.assertArrayEquals(arrayLikes, filmDBStorage.getFilmById(1L).getLikes().toArray(), "Лайк к" +
                " фильму не добавился");
    }

    @Test
    public void deleteLikeByFilmIdTest() {
        Film film = Film.builder()
                .id(1L)
                .name("TestName")
                .description("TestDescription")
                .releaseDate(LocalDate.of(2005, 7, 1))
                .duration(100)
                .genres(Set.of(new Genre(3L, "Мультфильм")))
                .likes(new HashSet<>())
                .mpa(new Mpa(4L, "PG-13"))
                .build();
        filmDBStorage.saveFilm(film);

        User user = User.builder()
                .id(1L)
                .name("TestName")
                .email("testMail@mail.ru")
                .login("TestLogin")
                .birthday(LocalDate.of(2001, 7, 5))
                .friends(new HashSet<>())
                .build();
        userDBStorage.saveUser(user);
        likeDBStorage.addLike(1L, 1L);
        Long[] arrayLikes = {1L};
        Assertions.assertArrayEquals(arrayLikes, filmDBStorage.getFilmById(1L).getLikes().toArray());
        likeDBStorage.deleteLike(1L, 1L);
        Long[] arrayDeletedLikes = {};
        Assertions.assertArrayEquals(arrayDeletedLikes, filmDBStorage.getFilmById(1L).getLikes().toArray(),
                "Не удалился лайк у фильма");
    }

    @Test
    public void readAllGenresTest() {
        Assertions.assertEquals(6, genreDBStorage.readAll().size(), "Неверное количество жанров");
    }

    @Test
    public void getGenreByIdTest() {
        Genre genre = genreDBStorage.readById(1L);
        Assertions.assertEquals("Комедия", genre.getName(), "Неверное имя жанра.");
    }

    @Test
    public void getGenreByFilmIDTest() {
        Film film = Film.builder()
                .id(1L)
                .name("TestName")
                .description("TestDescription")
                .releaseDate(LocalDate.of(2005, 7, 1))
                .duration(100)
                .genres(Set.of(new Genre(3L, "Мультфильм")))
                .likes(new HashSet<>())
                .mpa(new Mpa(4L, "PG-13"))
                .build();
        filmDBStorage.saveFilm(film);

        List<Genre> genres = new ArrayList<>(genreDBStorage.getGenresByFilmID(1L));
        Assertions.assertEquals("Мультфильм", genres.get(0).getName(), "Неверный жанр у фильма.");
    }

    @Test
    public void readAllMpaTest() {
        Assertions.assertEquals(5, mpaDBStorage.readAll().size(), "Неверное количество Mpa.");
    }

    @Test
    public void readMpaByIdTest() {
        Mpa mpa = new Mpa(1L, "G");
        Assertions.assertEquals(mpa, mpaDBStorage.readById(1L), "Получили неверный Mpa");
    }

    @Test
    public void readMpaByFilmIdTest() {
        Film film = Film.builder()
                .id(1L)
                .name("TestName")
                .description("TestDescription")
                .releaseDate(LocalDate.of(2005, 7, 1))
                .duration(100)
                .genres(Set.of(new Genre(3L, "Мультфильм")))
                .likes(new HashSet<>())
                .mpa(new Mpa(2L, "PG"))
                .build();
        filmDBStorage.saveFilm(film);
        Assertions.assertEquals(film.getMpa(), mpaDBStorage.readById(2L), "Неверный Mpa у фильма.");
    }
}