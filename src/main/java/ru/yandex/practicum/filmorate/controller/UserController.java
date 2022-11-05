package ru.yandex.practicum.filmorate.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private int idUser;
    private final Map<Integer, User> allUsers = new HashMap<>();

    //Добавляем пользователя
    @PostMapping(value = "/users")
    public User addUser(@Valid @RequestBody User user){
        if (user.getName().isBlank()){
            user.setName(user.getLogin());
        }
        idUser++;
        user.setId(idUser);
        allUsers.put(idUser, user);
        log.info("Добавлен пользователь");
        return allUsers.get(user.getId());
    }

    //обновление пользователя
    @PutMapping("/users")
    public User updateUser(@Valid @RequestBody User user) throws ValidationException {
            if(allUsers.containsKey(user.getId())){
                allUsers.put(user.getId(), user);
                log.info("Пользователь обновлен");
            }else {
                log.info("Пользователь не обновлен");
                throw  new ValidationException("Пользователь с таким ID отсутствует");
            }
        return allUsers.get(user.getId());
    }

    //получить всех пользователей
    @GetMapping("/users")
    public List<User> getAllUsers(){
        log.info("Все пользователи получены");
        return List.copyOf(allUsers.values());
    }

    public List<User> getListAllUsers(){
        return List.copyOf(allUsers.values()) ;
    }
}
