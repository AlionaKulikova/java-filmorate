package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private static final Map<Long, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> findAll() {
        return users.values();
    }

    @PostMapping
    public static User create(@RequestBody User user) {
        log.info("Создание пользователя: {}", user);
        validateUser(user);
        user.setId(getNextId());
        users.put(user.getId(), user);
        log.info("Пользователь успешно создан с ID: {}", user.getId());
        return user;
    }

    @PutMapping
    public User update(@RequestBody User newUser) {
        log.info("Обновление пользователя с ID: {}", newUser.getId());
        if (newUser.getId() == null) {
            log.error("Ошибка при обновлении: Id должен быть указан");
            throw new ConditionsNotMetException("Id должен быть указан");
        }
        if (users.containsKey(newUser.getId())) {
            User oldUser = users.get(newUser.getId());
            validateUser(newUser);
            oldUser.setEmail(newUser.getEmail());
            oldUser.setLogin(newUser.getLogin());
            oldUser.setName(newUser.getName());
            oldUser.setBirthday(newUser.getBirthday());
            log.info("Пользователь с ID: {} успешно обновлен", newUser.getId());
            return oldUser;
        }
        log.error("Ошибка при обновлении: Пользователь с id = {} не найден", newUser.getId());
        throw new NotFoundException("Пользователь с id = " + newUser.getId() + " не найден");
    }

    private static void validateUser(User user) {
        if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            log.error("Ошибка валидации: Email не может быть пустым и должна содержать символ @");
            throw new ConditionsNotMetException("Email не может быть пустым и должна содержать символ @");
        }
        if (user.getLogin() == null || user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            log.error("Ошибка валидации: Логин не может быть пустым и не должен содержать пробелы");
            throw new ConditionsNotMetException("Логин не может быть пустым и не должен содержать пробелы");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        if (user.getBirthday() == null || user.getBirthday().isAfter(LocalDate.now())) {
            log.error("Ошибка валидации: Дата рождения не может быть в будущем");
            throw new ConditionsNotMetException("Дата рождения не может быть в будущем");
        }
    }

    private static long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
