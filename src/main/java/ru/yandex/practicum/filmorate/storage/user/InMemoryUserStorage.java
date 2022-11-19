package ru.yandex.practicum.filmorate.storage.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validations.Validations;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage {
    private static final Logger log = LoggerFactory.getLogger(InMemoryUserStorage.class);
    private int idUser;
    private final Map<Integer, User> allUsers = new HashMap<>();

    public User addUser(User user) throws ValidationException {
        Validations.validateUser(user);
        idUser++;
        user.setId(idUser);
        allUsers.put(idUser, user);
        log.info("Добавлен пользователь");
        return allUsers.get(user.getId());
    }

    public User updateUser(User user) throws ValidationException {
        Validations.validateUser(user);
        if (allUsers.containsKey(user.getId())) {
            allUsers.put(user.getId(), user);
            log.info("Пользователь обновлен");
        } else {
            log.info("Пользователь не обновлен");
            throw new UserNotFoundException(String.format(
                    "Пользователь %s не найден",
                    user.getName()));
        }
        return allUsers.get(user.getId());
    }

    public List<User> getAllUsers() {
        log.info("Все пользователи получены");
        return List.copyOf(allUsers.values());
    }

    //получить пользователя по id
    public User getUser(int id) {
        if (allUsers.containsKey(id)) {
            log.info("Пользователь с ID = " + id + "получен");
        } else {
            log.info("Пользователь не получен");
            throw new UserNotFoundException(String.format(
                    "Пользователь с ID %s не найден", id));
        }
        return allUsers.get(id);
    }

    //удалить пользователя по id
    public void removeUser(int id) {
        if (allUsers.containsKey(id)) {
            allUsers.remove(id);
            log.info("Пользователь с ID = " + id + "удален");
        } else {
            log.info("Пользователь не получен");
            throw new UserNotFoundException(String.format(
                    "Пользователь с ID %s не найден", id));
        }
    }

    public Map<Integer, User> getAllUsersMap() {
        return allUsers;
    }
}
