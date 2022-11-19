package ru.yandex.practicum.filmorate.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class FilmService {
    private static final Logger log = LoggerFactory.getLogger(FilmService.class);
    private final InMemoryFilmStorage inMemoryFilmStorage;

    @Autowired
    public FilmService(InMemoryFilmStorage inMemoryFilmStorage) {
        this.inMemoryFilmStorage = inMemoryFilmStorage;
    }

    //пользователь ставит лайк фильму
    public void addLike(int id, int userId) {
        if (userId <= 0){
            log.info("id пользователя не корректно");
            throw new UserNotFoundException(String.format(
                    "Пользователь с ID %s не найден", id));
        } else if(inMemoryFilmStorage.getAllFilmMap().containsKey(id) || userId >= 0) {
            Film film = inMemoryFilmStorage.getAllFilmMap().get(id);
            film.getListOfUsersWhoHaveLiked().add(userId);
            log.info("Фильму " + film.getName() + " поставлен лайк пользователем с ID " + userId);
        } else{
            log.info("Фильм не получен");
            throw new FilmNotFoundException(String.format(
                    "Фильм с ID %s не найден или id пользователя не соответствут параметрам", id));
        }
    }

    //удаление лайка
    public void removeLike(int id, int userId) {
        if (userId <= 0){
            log.info("id пользователя не корректно");
            throw new UserNotFoundException(String.format(
                    "Пользователь с ID %s не найден", id));
        } else if(inMemoryFilmStorage.getAllFilmMap().containsKey(id)) {
            Film film = inMemoryFilmStorage.getAllFilmMap().get(id);
            film.getListOfUsersWhoHaveLiked().remove(userId);
            log.info("Фильму " + film.getName() + " поставлен лайк пользователем с ID " + userId);
        } else{
            log.info("Фильм не получен");
            throw new FilmNotFoundException(String.format(
                    "Фильм с ID %s не найден или id пользователя не соответствут параметрам", id));
        }
    }

    //возрат списка первых по количеству лайков N фильмов
    //ДОРАБОТАТЬ!!!!!!!!!!!!!
    public List<Film> getListOfPopularFilms(int count) {
        List<Film> listFilms = new ArrayList<>();
        Film thisFilm;
        for (Film film : inMemoryFilmStorage.getAllFilmMap().values()) {
            if (listFilms.isEmpty()) {
                listFilms.add(film);
            } else {
                if (inMemoryFilmStorage.getAllFilmMap().size() < count) {
                    count = listFilms.size();
                }
                for (int i = 0; i < count - 1; i++) {
                    thisFilm = listFilms.get(i);
                    if (film.getLike() > thisFilm.getLike() || thisFilm == null) {
                        listFilms.add(i, film);
                        break;
                    }
                }
            }
        }
        return listFilms;
    }

    public InMemoryFilmStorage getInMemoryFilmStorage() {
        return inMemoryFilmStorage;
    }
}
