package ru.yandex.practicum.filmorate.dao.user;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    User addUser(User user) throws ValidationException;

    User updateUser(User user) throws ValidationException;

    List<User> getAllUsers();

    User getUser(int id);

    void removeUser(int id);

    void addUserFiends(int id, int friendId);

    List<User> findFriends(int id);

    List<User> mutualFriends(int id, int otherId);

    void removeFriend(int id, int friendId);

    List<Film> recommendations(int id);
}
