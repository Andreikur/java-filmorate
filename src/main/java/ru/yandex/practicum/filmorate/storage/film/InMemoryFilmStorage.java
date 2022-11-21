package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validations.Validations;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
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
            throw new FilmNotFoundException(String.format(
                    "Фильм %s не найден",
                    film.getName()));
        }
        log.info("Фильм обновлен");
        return allFilms.get(film.getId());
    }

    public List<Film> getAllFilms() {
        log.info("Получены все фильмы");
        return List.copyOf(allFilms.values());
    }

    public Map<Integer, Film> getAllFilmMap() {
        return allFilms;
    }

    //получить фильм по id
    public Film getFilm(int id) {
        if (allFilms.containsKey(id)) {
            log.info("Фильм с ID = " + id + "получен");
        } else {
            log.info("Фильм не получен");
            throw new FilmNotFoundException(String.format(
                    "Фильм с ID %s не найден",
                    id));
        }
        return allFilms.get(id);
    }

    //Удаление фильма
    public void removeFilm(int id) {
        if (allFilms.containsKey(id)) {
            allFilms.remove(id);
            log.info("Фильм с ID = " + id + "удален");
        } else {
            log.info("Фильм не получен");
            throw new FilmNotFoundException(String.format(
                    "Фильм с ID %s не найден",
                    id));
        }
    }
}
