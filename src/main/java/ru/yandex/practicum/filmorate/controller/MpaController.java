package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

@RequestMapping(value = "/mpa")
@RestController
public class MpaController {

    private final FilmService filmService;

    @Autowired
    public MpaController(FilmService filmService){
        this.filmService = filmService;
    }

    /*@GetMapping
    public List<Mpa> getAllMpa(){
        return filmService.????;
    }

    @GetMapping("/{id}")
    public Mpa getMpa(@PathVariable("id") Integer id){
        return filmService.????;
    }*/
}
