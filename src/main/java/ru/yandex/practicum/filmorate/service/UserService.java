package ru.yandex.practicum.filmorate.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.auxiliary.SortingSet;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.util.*;

@Service
public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private final InMemoryUserStorage inMemoryUserStorage;

    @Autowired
    public UserService(InMemoryUserStorage inMemoryUserStorage) {
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    //добавить в друзья
    public void addFriend(int id, int friendId){
        if (inMemoryUserStorage.getAllUsersMap().containsKey(id) &&
                inMemoryUserStorage.getAllUsersMap().containsKey(friendId)) {
            User user = inMemoryUserStorage.getAllUsersMap().get(id);
            User userFriend = inMemoryUserStorage.getAllUsersMap().get(friendId);
            user.getListOfFriends().add(friendId);
            user.setListOfFriends(SortingSet.Sorting(user.getListOfFriends()));
            userFriend.getListOfFriends().add(id);
            userFriend.setListOfFriends(SortingSet.Sorting(userFriend.getListOfFriends()));
            log.info("Пользователи " + user.getName() + " и " + userFriend.getName() + " теперь друзья");
        } else {
            log.info("Пользователь не получен");
            throw new UserNotFoundException(String.format(
                    "Пользователь с ID %s или %s не найден",
                    id, friendId));
        }
    }

    //удалить из друзей
    public void removeFriend(int id, int friendId){
        User user = inMemoryUserStorage.getAllUsersMap().get(id);
        User userFriend = inMemoryUserStorage.getAllUsersMap().get(friendId);
        if (user.getListOfFriends().contains(friendId)) {
            user.getListOfFriends().remove(friendId);
            userFriend.getListOfFriends().remove(id);
            log.info("Пользователи " + user.getName() + " и " + userFriend.getName() + " больше не друзья");
        } else {
            log.info("Пользователь не являются друзьями");
            throw new UserNotFoundException(String.format(
                    "Пользователь с ID %s и %s не друзья",
                    id, friendId));
        }
    }

    //вернуть всех друзей пользователя
    public List<User> findFriends(int id){
        List<User> userFriends = new ArrayList<>();
        for(int idFriend : inMemoryUserStorage.getAllUsersMap().get(id).getListOfFriends()){
            userFriends.add(inMemoryUserStorage.getAllUsersMap().get(idFriend));
        }
        return userFriends;
    }

    // список общих друзей
    public List<User> mutualFriends (int id, int otherId){
        List<User> listMutualOfFriends = new ArrayList<>();
        for (int idFriends :inMemoryUserStorage.getAllUsersMap().get(id).getListOfFriends()){
            for(int idOtherFriends : inMemoryUserStorage.getAllUsersMap().get(otherId).getListOfFriends()){
                if(idFriends == idOtherFriends){
                    listMutualOfFriends.add(inMemoryUserStorage.getAllUsersMap().get(idFriends));
                    break;
                }
            }
        }
        return listMutualOfFriends;
    }

    public InMemoryUserStorage getInMemoryUserStorage() {
        return inMemoryUserStorage;
    }
}
