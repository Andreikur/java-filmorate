package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping(value = "/users")
@RestController
public class UserController {

    private final UserService userService;

    //Добавляем пользователя
    @PostMapping
    public User addUser(@Valid @RequestBody User user) throws ValidationException {
        return userService.getUserStorage().addUser(user);
    }

    //обновление пользователя
    @PutMapping
    public User updateUser(@Valid @RequestBody User user) throws ValidationException {
        return userService.getUserStorage().updateUser(user);
    }

    //получить всех пользователей
    @GetMapping
    public List<User> getAllUsers() {
        return userService.getUserStorage().getAllUsers();
    }

    //получить пользователя по id
    @GetMapping("/{id}")
    public User getUser(@PathVariable("id") Integer id) {
        return userService.getUserStorage().getUser(id);
    }

    //удалить пользователя


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
