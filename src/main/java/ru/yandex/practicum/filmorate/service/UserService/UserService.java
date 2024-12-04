package ru.yandex.practicum.filmorate.service.UserService;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserService {
    Collection<User> findAllUsers();

    User findUserByID(Long id);

    User createUser(User user);

    User updateUser(User user);

    void addFriend(Long userId, Long friendId);

    void deleteFriendById(Long idUser, Long idFriend);

    Collection<User> readAllFriendsByUserId(Long idUser);

    Collection<User> readAllCommonFriends(Long idUser1, Long idUser2);
}
