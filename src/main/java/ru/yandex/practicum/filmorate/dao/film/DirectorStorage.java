package ru.yandex.practicum.filmorate.dao.film;

import org.springframework.web.bind.annotation.PathVariable;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface DirectorStorage {

    Director addDirector(Director director);

    Director updateDirector(Director director);

    List<Director> getAllDirectors();

    Director getDirector(int id);

    void removeDirector(int id);
}
