package ru.yandex.practicum.filmorate.storage.film;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validations.Validations;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private static final Logger log = LoggerFactory.getLogger(InMemoryFilmStorage.class);
    private int idFilm;
    private final Map<Integer, Film> allFilms = new HashMap<>();

    //Добавляем фильм
    public Film addFilm(Film film) throws ValidationException {
        Validations.validateFilm(film);
        idFilm++;
        film.setId(idFilm);
        allFilms.put(idFilm, film);
        log.info("Фильм добавлен");
        return allFilms.get(film.getId());
    }

    public Film updateFilm(Film film) throws ValidationException {
        Validations.validateFilm(film);
        if (allFilms.containsKey(film.getId())) {
            allFilms.put(film.getId(), film);
        } else {
            log.info("Фильм не обновлен");
            throw new ValidationException("Фмльм отсутствует в коллекции");
        }
        log.info("Фильм обновлен");
        return allFilms.get(film.getId());
    }

    public List<Film> getAllFilms(){
        log.info("Получены все фильмы");
        return List.copyOf(allFilms.values());
    }

    public void removeFilm() {
    }
}
