package ru.yandex.practicum.filmorate.service.UserService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@Service
public class UserServiceManager implements UserService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public UserServiceManager(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    @Override
    public Collection<User> findAllUsers() {
        return userStorage.getAllUsers();
    }

    @Override
    public User findUserByID(Long userId) {
        return userStorage.getUserById(userId);
    }

    @Override
    public User createUser(User user) {
        log.info("Создание пользователя: {}", user);
        validateUser(user);
        user.setId(getNextId());

        return userStorage.saveUser(user);
    }

    @Override
    public User updateUser(User newUser) {
        log.info("Обновление пользователя с ID: {}", newUser.getId());
        if (newUser.getId() == null) {
            log.error("Ошибка при обновлении: Id должен быть указан");
            throw new ConditionsNotMetException("Id должен быть указан");
        }
        User oldUser = userStorage.updateUser(newUser);
        validateUser(newUser);
        oldUser.setEmail(newUser.getEmail());
        oldUser.setLogin(newUser.getLogin());
        oldUser.setName(newUser.getName());
        oldUser.setBirthday(newUser.getBirthday());
        log.info("Пользователь с ID: {} успешно обновлен", newUser.getId());

        return oldUser;
    }

    @Override
    public void addFriend(Long userId, Long friendId) {
        User user = userStorage.getUserById(userId);
        User friend = userStorage.getUserById(friendId);
        user.getFriends().add(friendId);
        friend.getFriends().add(userId);
        log.info("Пользователю с id " + userId + " добавлен друг с id" + friendId);
    }

    @Override
    public void deleteFriendById(Long idUser, Long idFriend) {
        User user = userStorage.getUserById(idUser);
        User friend = userStorage.getUserById(idFriend);
        user.getFriends().remove(idFriend);
        friend.getFriends().remove(idUser);
        log.info("Пользователь с id " + idUser + " и пользователь с id " + idFriend + " больше не друзья.");
    }

    @Override
    public Collection<User> readAllFriendsByUserId(Long idUser) {
        User user = userStorage.getUserById(idUser);
        Set<Long> idFriendsOfUser = user.getFriends();
        Collection<User> friendsOfUser = new ArrayList<>();
        for (Long id : idFriendsOfUser) {
            friendsOfUser.add(userStorage.getUserById(id));
        }
        log.info("Возвраён список друзей пользователя с id " + idUser);
        return friendsOfUser;
    }

    @Override
    public Collection<User> readAllCommonFriends(Long idUser1, Long idUser2) {
        Set<Long> idFriendsOfUsers = new HashSet<>(userStorage.getUserById(idUser1).getFriends());//получаем друзей 1-го//сразу в hashset
        idFriendsOfUsers.retainAll(userStorage.getUserById(idUser2).getFriends());//получаем друзей 2-го//b  тоже в hashset

        Collection<User> friendsOfUsers = new ArrayList<>();
        for (Long id : idFriendsOfUsers) {
            friendsOfUsers.add(userStorage.getUserById(id));
        }
        log.info("Возвращён список общих друзей пользователя с id " + idUser1 + " и пользователя с id" + idUser2);
        return friendsOfUsers;
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

    private long getNextId() {
        long currentMaxId = userStorage.getAllUsers().stream()
                .mapToLong(User::getId)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}