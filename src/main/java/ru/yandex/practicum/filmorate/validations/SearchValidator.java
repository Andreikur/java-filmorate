package ru.yandex.practicum.filmorate.validations;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.enums.FilmSearchOptions;
import ru.yandex.practicum.filmorate.exception.WrongSearchException;

import java.util.HashSet;
import java.util.Set;

@Component
public class SearchValidator {
    public void validateQuery(String query){
        if (query.equals("")) {
            throw new WrongSearchException("Должна быть указана строка запроса query");
        }
    }

    public Set<FilmSearchOptions> validateBy(Set<String> by){
        Set<FilmSearchOptions> params = new HashSet<>();
        if (by.isEmpty()) {
            throw new WrongSearchException("Должен быть указан параметр запроса by");
        }
        for (String b: by) {
            if (!FilmSearchOptions.has(b)) {
                throw new WrongSearchException("Параметр запроса by должен соответствовать возможным значениям");
            }
            params.add(FilmSearchOptions.valueOf(b.toUpperCase()));
        }
        return params;
    }
}
