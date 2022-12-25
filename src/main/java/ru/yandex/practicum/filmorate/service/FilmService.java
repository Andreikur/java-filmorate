package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import ru.yandex.practicum.filmorate.dao.event.EventStorage;
import ru.yandex.practicum.filmorate.model.EventEnum.OperationType;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.dao.film.FilmStorage;

@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final EventStorage eventStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, EventStorage eventStorage) {
        this.filmStorage = filmStorage;
        this.eventStorage = eventStorage;
    }

    //пользователь ставит лайк фильму
    public void addLike(int filmId, int userId) {
       filmStorage.addLike(filmId, userId);
       eventStorage.addLike(userId, OperationType.ADD, filmId);
    }

    //удаление лайка
    public void removeLike(int filmId, int userId) {
        filmStorage.removeLike(filmId, userId);
        eventStorage.addLike(userId, OperationType.REMOVE, filmId);
    }

    //возрат списка первых по количеству лайков N фильмов
    public List<Film> getListOfPopularFilms(int count, int genreId, int year) {
        return filmStorage.getListOfPopularFilms(count, genreId, year);
    }
    public List<Film> getDirectorFilmList(int directorId, String sortBy) {
        return filmStorage.getDirectorFilmList(directorId, sortBy);
    }

    public FilmStorage getFilmStorage() {
        return filmStorage;
    }


    public List<Film> getCommonFilms(int userId, int friendId) {
        return filmStorage.getCommonFilms(userId, friendId);
    }
}
