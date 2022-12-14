package ru.yandex.practicum.filmorate.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.dao.film.FilmStorage;

@Service
public class FilmService {
    private static final Logger log = LoggerFactory.getLogger(FilmService.class);
    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    //пользователь ставит лайк фильму
    public void addLike(int filmId, int userId) {
       filmStorage.addLike(filmId, userId);
    }

    //удаление лайка
    public void removeLike(int filmId, int userId) {
        filmStorage.removeLike(filmId, userId);
    }

    //возрат списка первых по количеству лайков N фильмов
    public List<Film> getListOfPopularFilms(int count) {
        return filmStorage.getListOfPopularFilms(count);
    }

    public FilmStorage getFilmStorage() {
        return filmStorage;
    }
}
