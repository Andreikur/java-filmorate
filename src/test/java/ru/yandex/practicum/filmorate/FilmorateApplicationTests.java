/*package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validations.Validations;

import java.time.LocalDate;

@SpringBootTest
public class FilmorateApplicationTests {
    Film film;
    User user;
    FilmController filmController;
    UserController userController;

    @BeforeEach
    void initialValues() {
        filmController = new FilmController();
        userController = new UserController();
        film = Film.builder()
                .name("Тестовый фильм 1")
                .description("Описание 1")
                .releaseDate(LocalDate.parse("1995-12-28"))
                .duration(100)
                .build();
        user = User.builder()
                .login("Пользователь1")
                .email("yandex@yandex.ru")
                .birthday(LocalDate.parse("1980-11-11"))
                .build();
    }

    @Test
    void realeaseDateValidationTest() {
        film.setReleaseDate(LocalDate.of(1600, 1, 1));
        Assertions.assertThrows(ValidationException.class, () -> Validations.validateFilm(film));
    }

    @Test
    void addingFilmTest() throws ValidationException {
        filmController.addFilm(film);
        Assertions.assertEquals(1, filmController.getAllFilms().size(), "Фильм не добавлен");
    }

    @Test
    void addingUserTest() throws ValidationException {
        userController.addUser(user);
        Assertions.assertEquals(1, userController.getAllUsers().size(), "Пользователь не добавлен");
    }

    @Test
    void loginWithSpacesValidationTest() {
        user.setLogin("Пользователь 1");
        Assertions.assertThrows(ValidationException.class, () -> Validations.validateUser(user));
    }
}*/