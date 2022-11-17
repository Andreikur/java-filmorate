package ru.yandex.practicum.filmorate.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import java.util.List;
import java.util.Set;

@Service
public class FilmService {
    private static final Logger log = LoggerFactory.getLogger(FilmService.class);
    private final InMemoryFilmStorage inMemoryFilmStorage;

    @Autowired
    public FilmService(InMemoryFilmStorage inMemoryFilmStorage){
        this.inMemoryFilmStorage = inMemoryFilmStorage;
    }

    //пользователь ставит лайк фильму
    public void addLike(int id, int userId){
        Film film = inMemoryFilmStorage.getAllFilms().get(id);
        film.getListOfUsersWhoHaveLiked().add(userId);
        log.info("Фильму " + film.getName() + " поставлен лайк пользователем с ID " + userId);
    }

    //удаление лайка
    public void removeLike(int id, int userId){
        Film film = inMemoryFilmStorage.getAllFilms().get(id);
        film.getListOfUsersWhoHaveLiked().remove(userId);
        log.info("Фильму " + film.getName() + " поставлен лайк пользователем с ID " + userId);
    }

    //возрат списка первых по количеству лайков N фильмов
    //ДОРАБОТАТЬ!!!!!!!!!!!!!
    public Film [] getListOfPopularFilms(int count){
        //Set<Film> listOfPopularFilms = new HashSet<>();
        Film [] popularFilms = new Film[count];
        List<Film> listFilms = inMemoryFilmStorage.getAllFilms();
        for (Film film : listFilms){
            for (int i = 0; i < popularFilms.length; i++){
            //for (Film filmArray : popularFilms){
                //if(film.getLike() )
            }
        }

        return popularFilms;
    }

    public InMemoryFilmStorage getInMemoryFilmStorage() {
        return inMemoryFilmStorage;
    }
}
