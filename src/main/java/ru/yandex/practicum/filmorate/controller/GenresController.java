package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

@RequestMapping(value = "/genres")
@RestController
public class GenresController {

    private final FilmService filmService;

    @Autowired
    public GenresController(FilmService filmService){
        this.filmService = filmService;
    }

    //Получить все жанры
    /*@GetMapping
    public List<Genre> getAllGenres(){
        return filmService.&&&&&&&;
    }

    //Получить жанр по id
    @GetMapping("/{id}")
    public Genre getGenre(@PathVariable("id") Integer id){
        return filmService.?????;
    }*/
}
