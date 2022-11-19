package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.List;

@RequestMapping(value = "/films")
@RestController
public class FilmController {

    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    //Добавляем фильм
    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) throws ValidationException {
        return filmService.getInMemoryFilmStorage().addFilm(film);
    }

    //Обновление фильма
    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) throws ValidationException {
       return filmService.getInMemoryFilmStorage().updateFilm(film);
    }

    //получить все фильмы
    @GetMapping
    public List<Film> getAllFilms() {
        return filmService.getInMemoryFilmStorage().getAllFilms();
    }

    //получить фильм
    @GetMapping("/{id}")
    public Film getFilm(@PathVariable("id") String id){
        int intId = Integer.parseInt(id);
        return filmService.getInMemoryFilmStorage().getFilm(intId);
    }

    //Удаление фильма ДОРАБОТАТЬ
    @DeleteMapping
    public void removeFilm(){

    }

    //пользователь ставит лайк фильму
    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable("id") String id, @PathVariable("userId") String userId){
        int intId = Integer.parseInt(id);
        int intUserId = Integer.parseInt(userId);
        filmService.addLike(intId, intUserId);
    }

    //удаление лайка
    @DeleteMapping("/{id}/like/{userId}")
    public void removeLike(@PathVariable("id") String id, @PathVariable("userId") String userId){
        int intId = Integer.parseInt(id);
        int intUserId = Integer.parseInt(userId);
        filmService.removeLike(intId, intUserId);
    }

    //возрат списка первых по количеству лайков N фильмов
    @GetMapping("/popular")
    public List<Film> getListOfPopularFilms(@PathVariable String count){
        int intCount = 10;
        intCount = Integer.parseInt(count);
        return filmService.getListOfPopularFilms(intCount);
    }

}
