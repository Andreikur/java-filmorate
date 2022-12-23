package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class SearchServiceTest {
    @Autowired
    private FilmController filmController;

    @BeforeEach
    void setUp() {
        initializingTestData();
    }

    @Test
    public void shouldSearchFilmsOnlyTitle() {
        Collection<Film> dbFilms = filmController.filmSearch("Стартрек", new HashSet<>(List.of("name")));
        assertEquals(1, dbFilms.size());
    }

    @Test
    public void shouldSearchFilmsOnlyDescription() {
        Collection<Film> dbFilms = filmController.filmSearch(
                "Звёздного", new HashSet<>(List.of("description")));
        assertEquals(1, dbFilms.size());
    }

    @Test
    public void shouldSearchAnywayEmpty() {
        Collection<Film> dbFilms = filmController.filmSearch(
                "Пусто", new HashSet<>(Arrays.asList("name", "description")));
        assertEquals(0, dbFilms.size());
    }

    @Test
    public void shouldSearchFilmsAnywayRegister() {
        Collection<Film> dbFilms = filmController.filmSearch("стартрек", new HashSet<>(List.of("name")));
        assertEquals(1, dbFilms.size());
    }

    private void initializingTestData() {
        Film film1 = new Film();
        film1.setName("Стартрек");
        film1.setDescription("Научно-фантастический фильм, действие которого происходит в мире Звёздного пути");
        film1.setReleaseDate(LocalDate.of(2009,5,7));
        film1.setDuration(90);
        film1.setMpa(new Mpa(1, "G"));
        try {
            filmController.addFilm(film1);
        } catch (ValidationException e) {
            throw new RuntimeException(e);
        }
    }
}
