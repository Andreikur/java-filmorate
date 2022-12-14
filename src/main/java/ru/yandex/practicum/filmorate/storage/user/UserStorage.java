package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Map;

public interface UserStorage {
    User addUser(User user) throws ValidationException;

    User updateUser(User user) throws ValidationException;

    List<User> getAllUsers();

    User getUser(int id);

    void removeUser(int id);

    //Map<Integer, User> getAllUsersMap();

    void addUserFiends (int id, int friendId);

    List<User> findFriends(int id);

    List<User> mutualFriends (int id, int otherId);

    void removeFriend(int id, int friendId);
}
