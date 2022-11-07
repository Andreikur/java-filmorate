package ru.yandex.practicum.filmorate.validations;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

public class ValidationsUser {

    public void validateUser(User user) throws ValidationException{
        if (user.getLogin().contains(" ")){
            throw  new ValidationException("Логин не может содержать пробелов");
        }
        if (user.getName().isBlank()){
            user.setName(user.getLogin());
        }
    }
}
