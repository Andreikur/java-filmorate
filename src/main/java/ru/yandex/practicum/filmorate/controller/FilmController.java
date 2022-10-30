package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;

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
    public Film addFilm(@RequestBody Film film){
        idFilm++;
        film.setId(idFilm);
        allFilms.put(idFilm, film);
        log.info("Добавлен фильм");
        return film;
    }

    //Обновление фильма
    @PutMapping("/films")
    public Film updateFilm(@RequestBody Film film){
        if(allFilms.containsKey(film.getId())){
            allFilms.put(film.getId(), film);
        }else {
            idFilm++;
            allFilms.put(idFilm, film);
        }
        log.info("Фильм обновлен");
        return film;
    }

    //получить все фильмы
    @GetMapping("/films")
    public List<Film> getAllFilms(){
        log.info("Получены все фильмы");
        return List.copyOf(allFilms.values());
    }


}
