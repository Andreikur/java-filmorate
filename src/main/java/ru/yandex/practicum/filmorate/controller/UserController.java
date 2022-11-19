package ru.yandex.practicum.filmorate.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@RequestMapping(value = "/users")
@RestController
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    //Добавляем пользователя
    @PostMapping
    public User addUser(@Valid @RequestBody User user) throws ValidationException {
        return userService.getInMemoryUserStorage().addUser(user);
    }

    //обновление пользователя
    @PutMapping
    public User updateUser(@Valid @RequestBody User user) throws ValidationException {
        return userService.getInMemoryUserStorage().updateUser(user);
    }

    //получить всех пользователей
    @GetMapping
    public List<User> getAllUsers() {
        return userService.getInMemoryUserStorage().getAllUsers();
    }

    //получить пользователя по id
    @GetMapping("/{id}")
    public User getUser(@PathVariable("id") String id) {
        int intId = Integer.parseInt(id);
        return userService.getInMemoryUserStorage().getUser(intId);
    }

    //удалить пользователя
    @DeleteMapping
    public void removeUser() {
        userService.getInMemoryUserStorage().removeUser();
    }

    //добавления в друзья
    @PutMapping("/{id}/friends/{friendId}")
    public void addFriends(@PathVariable("id") String id, @PathVariable("friendId") String friendId) {
        int intId = Integer.parseInt(id);
        int intFriendId = Integer.parseInt(friendId);
        userService.addFriend(intId, intFriendId);
    }

    //удаление из друзей
    @DeleteMapping("/{id}/friends/{friendId}")
    public void removeFriend(@PathVariable("id") String id, @PathVariable("friendId") String friendId) {
        int intId = Integer.parseInt(id);
        int intFriendId = Integer.parseInt(friendId);
        userService.removeFriend(intId, intFriendId);
    }

    //возвращаем список пользователей являющихся его друзъями
    @GetMapping("/{id}/friends")
    public List<User> findFriends(@PathVariable("id") String id) {
        int intId = Integer.parseInt(id);
        return userService.findFriends(intId);
    }

    // список общих друзей
    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> mutualFriends(@PathVariable("id") String id, @PathVariable String otherId) {
        int intId = Integer.parseInt(id);
        int intOtherId = Integer.parseInt(otherId);
        return userService.mutualFriends(intId, intOtherId);
    }
}
