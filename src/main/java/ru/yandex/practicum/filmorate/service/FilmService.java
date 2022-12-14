package ru.yandex.practicum.filmorate.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
//import ru.yandex.practicum.filmorate.comparator.FilmLikeComparator;

import java.util.*;

import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

@Service
public class FilmService {
    private static final Logger log = LoggerFactory.getLogger(FilmService.class);
    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    //пользователь ставит лайк фильму
    /*public void addLike(int id, int userId) {
        if (userId <= 0) {
            log.info("id пользователя не корректно");
            throw new UserNotFoundException(String.format(
                    "Пользователь с ID %s не найден", id));
        } else if (inMemoryFilmStorage.getAllFilmMap().containsKey(id)) {
            Film film = inMemoryFilmStorage.getAllFilmMap().get(id);
            film.getListOfUsersWhoHaveLiked().add(userId);
            log.info("Фильму " + film.getName() + " поставлен лайк пользователем с ID " + userId);
        } else {
            log.info("Фильм не получен");
            throw new FilmNotFoundException(String.format(
                    "Фильм с ID %s не найден или id пользователя не соответствут параметрам", id));
        }
    }*/

    //удаление лайка
    /*public void removeLike(int id, int userId) {
        if (userId <= 0) {
            log.info("id пользователя не корректно");
            throw new UserNotFoundException(String.format(
                    "Пользователь с ID %s не найден", id));
        } else if (inMemoryFilmStorage.getAllFilmMap().containsKey(id)) {
            Film film = inMemoryFilmStorage.getAllFilmMap().get(id);
            film.getListOfUsersWhoHaveLiked().remove(userId);
            log.info("Фильму " + film.getName() + " поставлен лайк пользователем с ID " + userId);
        } else {
            log.info("Фильм не получен");
            throw new FilmNotFoundException(String.format(
                    "Фильм с ID %s не найден или id пользователя не соответствут параметрам", id));
        }
    }*/

    //возрат списка первых по количеству лайков N фильмов
    public List<Film> getListOfPopularFilms(int count) {
        /*List<Film> listFilms = new ArrayList<>();
        for (Film film : f.getAllFilmMap().values()) {
            listFilms.add(film);
        }
        if (listFilms.size() < count) {
            count = listFilms.size();
        }
        Collections.sort(listFilms, new FilmLikeComparator().reversed());*?
        return listFilms.subList(0, count);*/
        return filmStorage.getListOfPopularFilms(count);
    }

    public FilmStorage getFilmStorage() {
        return filmStorage;
    }

    //получить все жанры
    //public List<Genre> getAllGenres(){
    //    return filmService.&&&&&&&;
    //}


}
