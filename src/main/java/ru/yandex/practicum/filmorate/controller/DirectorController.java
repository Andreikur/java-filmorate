package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.service.DirectorService;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping(value = "/directors")
@RestController
public class DirectorController {

    private final DirectorService directorService;

    //создать режисера
    @PostMapping
    public Director addDirector(@Valid @RequestBody Director director) {
        return directorService.addDirectors(director);
    }

    //Обновление режисера
    @PutMapping
    public Director updateDirectors(@Valid @RequestBody Director director) {
        return directorService.updateDirectors(director);
    }

    //Получить все жанры
    @GetMapping
    public List<Director> getAllDirectors() {
        return directorService.getAllDirectors();
    }

    //Получить режисера по id
    @GetMapping("/{id}")
    public Director getDirector(@PathVariable("id") Integer id) {
        return directorService.getDirector(id);
    }

    //Удаление режисера
    @DeleteMapping("/{id}")
    public void removeDirector(@PathVariable("id") Integer id) {
        directorService.removeDirector(id);
    }
}
