package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FilmStorage {

    //Добавляем фильм
    Film addFilm(Film film) throws ValidationException;
    Film updateFilm(Film film) throws ValidationException;
    public List<Film> getAllFilms();
    public Film getFilm(int id);
    void removeFilm();
}
