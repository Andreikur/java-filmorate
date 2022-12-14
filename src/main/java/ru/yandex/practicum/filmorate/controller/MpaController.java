package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.dao.film.FilmStorage;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping(value = "/mpa")
@RestController
public class MpaController {

    private final FilmStorage filmStorage;

    @GetMapping
    public List<Mpa> getAllMpa(){
        return filmStorage.getAllMpa();
    }

    @GetMapping("/{id}")
    public Mpa getMpa(@PathVariable("id") Integer id){
        return filmStorage.getMpa(id);
    }
}
