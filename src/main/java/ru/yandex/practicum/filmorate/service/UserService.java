package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.event.EventStorage;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.dao.user.UserStorage;

import java.util.*;


@Service
public class UserService {
    private final UserStorage userStorage;
    private final EventStorage eventStorage;


    @Autowired
    public UserService(UserStorage userStorage, EventStorage eventStorage) {
        this.userStorage = userStorage;
        this.eventStorage = eventStorage;
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

    public List<Event> event(int id) {
        if(userStorage.getUser(id)==null){
            throw new UserNotFoundException(id+" user not found");
        }
        return eventStorage.get(id);
    }
}
