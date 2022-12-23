package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.dao.user.UserStorage;

import java.util.*;

@Service
public class UserService {
    private final UserStorage userStorage;


    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    //добавить в друзья
    public void addFriend(int id, int friendId) {
        userStorage.addUserFiends(id, friendId);
    }

    //удалить из друзей
    public void removeFriend(int id, int friendId) {
        userStorage.removeFriend(id, friendId);
    }

    //вернуть всех друзей пользователя
    public List<User> findFriends(int id) {
        return userStorage.findFriends(id);
    }

    // список общих друзей
    public List<User> mutualFriends(int id, int otherId) {
        return userStorage.mutualFriends(id, otherId);
    }

    //рекомендации фильмов другому пользователю по интересам данного пользователя
    public List<Film> recommendations(int id) {
        return userStorage.recommendations(id);
    }

    public UserStorage getUserStorage() {
        return userStorage;
    }
}
