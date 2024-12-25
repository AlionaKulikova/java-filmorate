/*package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService.UserService;
import ru.yandex.practicum.filmorate.service.UserService.UserServiceManager;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserControllerTest {

    @Test
    void shouldSave() {
        User user = User.builder()
                .email("testOne@mail.ru")
                .login("loginTestOne")
                .name("NameTestOne")
                .birthday(LocalDate.of(1999, 7, 4))
                .build();
        FilmStorage filmStorage = new InMemoryFilmStorage();
        UserStorage userStorage = new InMemoryUserStorage();
        UserService userService = new UserServiceManager(filmStorage, userStorage);
        UserController userController = new UserController(userService);
        User saveUser = userController.create(user);

        assertEquals(user, saveUser);
    }

    @Test
    void shouldUpdate() {
        User user = User.builder()
                .email("testTwo@mail.ru")
                .login("loginTestTwo")
                .name("NameTestTwo")
                .birthday(LocalDate.of(2000, 7, 5))
                .build();

        FilmStorage filmStorage = new InMemoryFilmStorage();
        UserStorage userStorage = new InMemoryUserStorage();
        UserService userService = new UserServiceManager(filmStorage, userStorage);
        UserController userController = new UserController(userService);
        User saveUser = userController.create(user);

        saveUser.setEmail("testThree@mail.ru");
        saveUser.setLogin("loginTestThree");
        saveUser.setName("NameTestThree");
        saveUser.setBirthday(LocalDate.of(1998, 7, 5));

        User updateUser = userController.create(saveUser);

        assertEquals(saveUser, updateUser);
    }
}
*/