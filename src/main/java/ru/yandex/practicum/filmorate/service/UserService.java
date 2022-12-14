package ru.yandex.practicum.filmorate.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.dao.user.UserStorage;

import java.util.*;

@Service
public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);
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

    public UserStorage getUserStorage() {
        return userStorage;
    }
}
