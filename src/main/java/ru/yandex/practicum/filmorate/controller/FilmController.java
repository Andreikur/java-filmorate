package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping(value = "/films")
@RestController
public class FilmController {

    private final FilmService filmService;

    //Добавляем фильм
    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) throws ValidationException {
        return filmService.getFilmStorage().addFilm(film);
    }

    //Обновление фильма
    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) throws ValidationException {
        return filmService.getFilmStorage().updateFilm(film);
    }

    //получить все фильмы
    @GetMapping
    public List<Film> getAllFilms() {
        return filmService.getFilmStorage().getAllFilms();
    }

    //получить фильм
    @GetMapping("/{id}")
    public Film getFilm(@PathVariable("id") Integer id) {
        return filmService.getFilmStorage().getFilm(id);
    }

    //Удаление фильма
    @DeleteMapping("/{id}")
    public void removeFilm(@PathVariable("id") Integer id) {
        filmService.getFilmStorage().removeFilm(id);
    }

    //пользователь ставит лайк фильму
    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable("id") Integer id, @PathVariable("userId") Integer userId) {
        filmService.addLike(id, userId);
    }

    //удаление лайка
    @DeleteMapping("/{id}/like/{userId}")
    public void removeLike(@PathVariable("id") Integer id, @PathVariable("userId") Integer userId) {
        filmService.removeLike(id, userId);
    }

    //возрат списка первых по количеству лайков N фильмов
    @GetMapping("/popular")
    public List<Film> getListOfPopularFilms(@RequestParam(required = false) Integer count) {

            if (count == null) {
            count = 10;
        }
        return filmService.getListOfPopularFilms(count);
    }

}
