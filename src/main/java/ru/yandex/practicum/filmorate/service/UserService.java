package ru.yandex.practicum.filmorate.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

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
    public void addFriend(int id, int friendId){
        userStorage.addUserFiends(id, friendId);
        //////////////////
        /*if (userStorage.getAllUsersMap().containsKey(id) &&
                userStorage.getAllUsersMap().containsKey(friendId)) {
            User user = userStorage.getAllUsersMap().get(id);
            User userFriend = userStorage.getAllUsersMap().get(friendId);
            user.getListOfFriends().add(friendId);
            userFriend.getListOfFriends().add(id);
            log.info("Пользователи " + user.getName() + " и " + userFriend.getName() + " теперь друзья");
        } else {
            log.info("Пользователь не получен");
            throw new UserNotFoundException(String.format(
                    "Пользователь с ID %s или %s не найден",
                    id, friendId));
        }*/
    }

    //удалить из друзей
    public void removeFriend(int id, int friendId){
        userStorage.removeFriend(id, friendId);
        /*User user = userStorage.getAllUsersMap().get(id);
        User userFriend = userStorage.getAllUsersMap().get(friendId);
        if (user.getListOfFriends().contains(friendId)) {
            user.getListOfFriends().remove(friendId);
            userFriend.getListOfFriends().remove(id);
            log.info("Пользователи " + user.getName() + " и " + userFriend.getName() + " больше не друзья");
        } else {
            log.info("Пользователь не являются друзьями");
            throw new UserNotFoundException(String.format(
                    "Пользователь с ID %s и %s не друзья",
                    id, friendId));
        }*/
    }

    //вернуть всех друзей пользователя
    public List<User> findFriends(int id){
        return userStorage.findFriends(id);
    }

    // список общих друзей
    public List<User> mutualFriends (int id, int otherId){
        return userStorage.mutualFriends(id,otherId);
        /*List<User> listMutualOfFriends = new ArrayList<>();
        for (int idFriends : userStorage.getAllUsersMap().get(id).getListOfFriends()){
            for(int idOtherFriends : userStorage.getAllUsersMap().get(otherId).getListOfFriends()){
                if(idFriends == idOtherFriends){
                    listMutualOfFriends.add(userStorage.getAllUsersMap().get(idFriends));
                    break;
                }
            }
        }
        return listMutualOfFriends;*/
    }

    public UserStorage getUserStorage() {
        return userStorage;
    }
}
