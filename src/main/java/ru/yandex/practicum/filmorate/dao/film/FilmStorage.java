package ru.yandex.practicum.filmorate.dao.film;

import ru.yandex.practicum.filmorate.enums.FilmSearchOptions;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface FilmStorage {

    Film addFilm(Film film) throws ValidationException;

    Film updateFilm(Film film) throws ValidationException;

    List<Film> getAllFilms();

    Film getFilm(int id);

    void removeFilm(int id);

    List<Film> getListOfPopularFilms(int count);

    void addLike(int id, int userId);

    void removeLike(int filmId, int userId);

    List<Mpa> getAllMpa();

    Mpa getMpa(int id);

    List<Genre> getAllGenre();

    Genre getGenre(int id);

    Collection<Film> getSortedFilmFromSearch(String query, Set<FilmSearchOptions> params);
}
