package ru.yandex.practicum.filmorate.controller;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FilmController {

    private int idFilm;
    private final Map<Integer, Film> allFilm = new HashMap<>();

    public void addFilm(Film film){
        idFilm++;
        allFilm.put(idFilm, film);
    }

    public void updateFilm(int idFilm){

    }

    private List<Film> getAllFilm(){
        return List.copyOf(allFilm.values());
    }


}
