package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.director.DirectorStorage;
import ru.yandex.practicum.filmorate.model.Director;

import java.util.List;

@Service
public class DirectorService {

    private final DirectorStorage directorStorage;

    @Autowired
    public DirectorService(DirectorStorage directorStorage) {
        this.directorStorage = directorStorage;
    }

    //создать режисера
    public Director addDirectors(Director director) {
        return directorStorage.addDirector(director);
    }

    //Обновление режисера
    public Director updateDirectors(Director director) {
        return directorStorage.updateDirector(director);
    }

    //Получить все жанры
    public List<Director> getAllDirectors() {
        return directorStorage.getAllDirectors();
    }

    //Получить режисера по id
    public Director getDirector(int id) {
        return directorStorage.getDirector(id);
    }

    //Удаление режисера
    public void removeDirector(int id) {
        directorStorage.removeDirector(id);
    }

}
