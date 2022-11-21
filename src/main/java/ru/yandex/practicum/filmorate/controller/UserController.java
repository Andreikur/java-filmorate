package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;

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
    public User getUser(@PathVariable("id") Integer id) {
        return userService.getInMemoryUserStorage().getUser(id);
    }

    //удалить пользователя
    @DeleteMapping("/{id}")
    public void removeUser(@PathVariable("id") Integer id) {
        userService.getInMemoryUserStorage().removeUser(id);
    }

    //добавления в друзья
    @PutMapping("/{id}/friends/{friendId}")
    public void addFriends(@PathVariable("id") Integer id, @PathVariable("friendId") Integer friendId) {
        userService.addFriend(id, friendId);
    }

    //удаление из друзей
    @DeleteMapping("/{id}/friends/{friendId}")
    public void removeFriend(@PathVariable("id") Integer id, @PathVariable("friendId") Integer friendId) {
        userService.removeFriend(id, friendId);
    }

    //возвращаем список пользователей являющихся его друзъями
    @GetMapping("/{id}/friends")
    public List<User> findFriends(@PathVariable("id") Integer id) {
        return userService.findFriends(id);
    }

    // список общих друзей
    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> mutualFriends(@PathVariable("id") Integer id, @PathVariable Integer otherId) {
        return userService.mutualFriends(id, otherId);
    }
}
