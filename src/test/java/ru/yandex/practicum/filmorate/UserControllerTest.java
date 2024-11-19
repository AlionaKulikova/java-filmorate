package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.User;

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

        User saveUser = UserController.create(user);

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

        final User saveUser = UserController.create(user);
        saveUser.setEmail("testThree@mail.ru");
        saveUser.setLogin("loginTestThree");
        saveUser.setName("NameTestThree");
        saveUser.setBirthday(LocalDate.of(1998, 7, 5));

        final User updateUser = UserController.create(saveUser);

        assertEquals(saveUser, updateUser);
    }
}
