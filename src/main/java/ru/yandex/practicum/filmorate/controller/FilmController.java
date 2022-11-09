package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validations.Validations;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping(value = "/films")
@RestController
public class FilmController {

    private static final Logger log = LoggerFactory.getLogger(FilmController.class);
    private int idFilm;
    private final Map<Integer, Film> allFilms = new HashMap<>();

    //Добавляем фильм
    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) throws ValidationException {
        Validations.validateFilm(film);
        idFilm++;
        film.setId(idFilm);
        allFilms.put(idFilm, film);
        log.info("Фильм добавлен");
        return allFilms.get(film.getId());
    }

    //Обновление фильма
    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) throws ValidationException {
        Validations.validateFilm(film);
        if (allFilms.containsKey(film.getId())) {
            allFilms.put(film.getId(), film);
        } else {
            log.info("Фильм не обновлен");
            throw new ValidationException("Фмльм отсутствует в коллекции");
        }
        log.info("Фильм обновлен");
        return allFilms.get(film.getId());
    }

    //получить все фильмы
    @GetMapping
    public List<Film> getAllFilms() {
        log.info("Получены все фильмы");
        return List.copyOf(allFilms.values());
    }
}
