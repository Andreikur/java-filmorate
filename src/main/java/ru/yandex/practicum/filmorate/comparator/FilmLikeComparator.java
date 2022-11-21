package ru.yandex.practicum.filmorate.comparator;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Comparator;

public class FilmLikeComparator implements Comparator<Film> {
    @Override
    public int compare(Film film1, Film film2) {
        return Integer.compare(film1.getLike(), film2.getId());
    }
}
