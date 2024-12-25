package ru.yandex.practicum.filmorate.service.UserService;

import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;

public interface UserService {
    List<UserDto> findAllUsers();

    UserDto findUserByID(Long id);

    UserDto createUser(User user);

    UserDto updateUser(User user);

    void addFriend(Long userId, Long friendId);

    void deleteFriendById(Long idUser, Long idFriend);

    Collection<UserDto> readAllFriendsByUserId(Long idUser);

    Collection<UserDto> readAllCommonFriends(Long idUser1, Long idUser2);
}
