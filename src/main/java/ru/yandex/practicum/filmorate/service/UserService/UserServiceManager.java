package ru.yandex.practicum.filmorate.service.UserService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserServiceManager implements UserService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public UserServiceManager(@Qualifier("FilmDbStorage") FilmStorage filmStorage,
                              @Qualifier("UserDbStorage") UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    @Override
    public List<UserDto> findAllUsers() {
        log.info("Получение всех пользователей.");
        return userStorage.getAllUsers()
                .stream()
                .map(UserMapper::mapToUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto findUserByID(Long userId) {
        log.info("Получение пользователя по id = {} ", userId);
        return UserMapper.mapToUserDto(userStorage.getUserById(userId));
    }

    @Override
    public UserDto createUser(User user) {
        log.info("Создание пользователя: {}", user);
        validateUser(user);
        user.setId(getNextId());

        return UserMapper.mapToUserDto(userStorage.saveUser(user));
    }

    @Override
    public UserDto updateUser(User newUser) {
        log.info("Обновление пользователя с ID: {}", newUser.getId());
        if (newUser.getId() == null) {
            log.error("Ошибка при обновлении пользователя. Id не указан");
            throw new ConditionsNotMetException("Id пользователя должен быть указан");
        }
        User oldUser = userStorage.updateUser(newUser);
        validateUser(newUser);
        oldUser.setEmail(newUser.getEmail());
        oldUser.setLogin(newUser.getLogin());
        oldUser.setName(newUser.getName());
        oldUser.setBirthday(newUser.getBirthday());
        log.info("Пользователь с ID: {} успешно обновлен", newUser.getId());

        return UserMapper.mapToUserDto(oldUser);
    }

    @Override
    public void addFriend(Long userId, Long friendId) {
        userStorage.userAddFriend(userId, friendId);
        log.info("Пользователю с id " + userId + " добавлен друг с id" + friendId);
    }

    @Override
    public void deleteFriendById(Long idUser, Long idFriend) {
        userStorage.userDeleteFriend(idUser, idFriend);
        log.info("Пользователь с id " + idUser + " и пользователь с id " + idFriend + " больше не друзья.");
    }

    @Override
    public Collection<UserDto> readAllFriendsByUserId(Long idUser) {
        User user = userStorage.getUserById(idUser);
        Set<Long> idFriendsOfUser = user.getFriends();
        Collection<UserDto> friendsOfUser = new ArrayList<>();
        for (Long id : idFriendsOfUser) {
            friendsOfUser.add(UserMapper.mapToUserDto(userStorage.getUserById(id)));
        }
        friendsOfUser.forEach(friend -> log.info(friend.toString()));
        log.info("Возврщаем список друзей пользователя с id " + idUser);

        return friendsOfUser;
    }

    @Override
    public Collection<UserDto> readAllCommonFriends(Long idUser1, Long idUser2) {
        Set<Long> idFriendsOfUsers = new HashSet<>(userStorage.getUserById(idUser1).getFriends());
        idFriendsOfUsers.retainAll(userStorage.getUserById(idUser2).getFriends());
        Collection<UserDto> friendsOfUsers = new ArrayList<>();
        for (Long id : idFriendsOfUsers) {
            friendsOfUsers.add(UserMapper.mapToUserDto(userStorage.getUserById(id)));
        }
        log.info("Возвращаем список общих друзей пользователя с id " + idUser1 + " и пользователя с id" + idUser2);

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

    private Long getNextId() {
        Long currentMaxId = (long) Math.toIntExact(userStorage.getAllUsers().stream()
                .mapToLong(User::getId)
                .max()
                .orElse(0));

        return ++currentMaxId;
    }
}