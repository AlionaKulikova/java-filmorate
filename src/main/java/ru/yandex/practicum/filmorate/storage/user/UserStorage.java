package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    List<User> getAllUsers();

    User getUserById(Long id);

    User saveUser(User user);

    User updateUser(User user);

    void userAddFriend(Long userId, Long friendId);

    void userDeleteFriend(Long userId, Long friendId);

    public List<User> getAllFriendByUserId(Long id);
}
