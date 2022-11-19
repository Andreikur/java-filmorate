package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    public User addUser(User user) throws ValidationException;
    public User updateUser(User user) throws ValidationException;
    public List<User> getAllUsers();
    public void removeUser();
}
