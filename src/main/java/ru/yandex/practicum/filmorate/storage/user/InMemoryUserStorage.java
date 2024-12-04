package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    private static final Map<Long, User> users = new HashMap<>();

    @Override
    public Collection<User> getAllUsers() {
        log.info("Получение пользователей");
        return users.values();
    }

    @Override
    public User getUserById(Long id) {
        if (users.containsKey(id)) {
            log.info("Получили пользователя c id " + id);

            return users.get(id);
        } else {
            log.error("Пользователь c id " + id + " не получен");
            throw new NotFoundException("Пользователь c id " + id + " не найден");
        }
    }

    @Override
    public User saveUser(User user) {
        users.put(user.getId(), user);
        log.info("Пользователь успешно создан с ID: {}", user.getId());

        return user;
    }

    @Override
    public User updateUser(User newUser) {
        if (users.containsKey(newUser.getId())) {
            return users.get(newUser.getId());
        }
        log.error("Ошибка при обновлении: Пользователь с id = {} не найден", newUser.getId());
        throw new NotFoundException("Пользователь с id = " + newUser.getId() + " не найден");
    }
}