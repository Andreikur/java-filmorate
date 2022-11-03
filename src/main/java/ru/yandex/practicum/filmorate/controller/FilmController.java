package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class FilmController {

    private static final Logger log = LoggerFactory.getLogger(FilmController.class);

    private int idFilm;
    private final Map<Integer, Film> allFilms = new HashMap<>();


    //Добавляем фильм
    @PostMapping(value = "/films")
    public Film addFilm(@Valid @RequestBody Film film){
        try {
            if (film.getReleaseDate().isAfter(LocalDate.parse("1895-12-28"))) {
                idFilm++;
                film.setId(idFilm);
                allFilms.put(idFilm, film);
                log.info("Фильм добавлен");
            } else {
                HttpStatus.resolve(500);
                log.info("Фильм не добавлен");
                throw  new ValidationException("Дата релиза не может быть раньше 28 декабря 1895");
            }
        } catch (ValidationException e){
            System.out.println(e.getMessage());
        }
        return allFilms.get(film.getId()) ;
    }

    //Обновление фильма
    @PutMapping("/films")
    public Film updateFilm(@Valid @RequestBody Film film){
        try {
            if (film.getReleaseDate().isAfter(LocalDate.parse("1895-12-28"))){
                if(allFilms.containsKey(film.getId())){
                    allFilms.put(film.getId(), film);
                }else {
                    idFilm++;
                    allFilms.put(idFilm, film);
                }
                log.info("Фильм обновлен");
            } else{
                HttpStatus.resolve(500);
                log.info("Фильм не обновлен");
                throw  new ValidationException("Дата релиза не может быть раньше 28 декабря 1895");
            }
        } catch (ValidationException e){
            System.out.println(e.getMessage());
        }
        return allFilms.get(film.getId());
    }

    //получить все фильмы
    @GetMapping("/films")
    public List<Film> getAllFilms(){
        log.info("Получены все фильмы");
        return List.copyOf(allFilms.values());
    }


}
