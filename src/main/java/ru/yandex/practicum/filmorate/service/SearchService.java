package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.constants.UsedStorageConsts;
import ru.yandex.practicum.filmorate.dao.film.FilmStorage;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validations.SearchValidator;

import java.util.Collection;
import java.util.Set;

@Service
public class SearchService {
    private final SearchValidator searchValidator;
    private final FilmStorage filmStorage;

    public SearchService(
            SearchValidator searchValidator,
            @Qualifier(UsedStorageConsts.QUALIFIER) FilmStorage filmStorage) {
        this.searchValidator = searchValidator;
        this.filmStorage = filmStorage;
    }

    /**
     * Возвращает коллекцию фильмов,
     * при поисковом запросе с параметрами
     * @param query - подстрока, которую необходимо найти (без учёта регистра)
     * @param by - ключи, задающие таблицы, в которых необходимо производить поиск
     * */
    public Collection<Film> filmSearch(String query, Set<String> by) {
        searchValidator.validateQuery(query);
        return filmStorage.getSortedFilmFromSearch(query, searchValidator.validateBy(by));
    }
}