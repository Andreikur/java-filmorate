package ru.yandex.practicum.filmorate.validations;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

public class Validations {

    public static void validateUser(User user) throws ValidationException{
        if (user.getLogin().contains(" ")){
            throw  new ValidationException("Логин не может содержать пробелов");
        }
        if (user.getName().isBlank()){
            user.setName(user.getLogin());
        }
    }

    public static void validateFilm(Film film) throws ValidationException{
        if (film.getReleaseDate().isBefore(LocalDate.parse("1895-12-28"))){
            throw  new ValidationException("Дата релиза не может быть раньше 28 декабря 1895");
        }
    }
}
