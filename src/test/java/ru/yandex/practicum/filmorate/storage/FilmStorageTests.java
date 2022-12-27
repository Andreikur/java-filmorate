/*package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.dao.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.dao.user.UserDbStorage;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmStorageTests {

    private final FilmDbStorage filmDbStorage;

    private final UserDbStorage userDbStorage;

    private Film film;
    private Film filmForUpdate;
    private User user;

    @BeforeEach
    public void initEach(){
        film = Film.builder()
                .name("filmName1")
                .description("description1")
                .releaseDate(LocalDate.of(2000, 12, 30))
                .duration(110)
                .mpa(new Mpa(1, "G"))
                .genres(null)
                .build();

        filmForUpdate = Film.builder()
                .id(999)
                .name("film2")
                .description(("description2"))
                .releaseDate(LocalDate.of(2021, 3, 5))
                .mpa(new Mpa(1, "G"))
                .genres(null)
                .build();

        user = User.builder()
                .id(1)
                .email("mail1@yandex.ru")
                .login("login1")
                .name("user1")
                .birthday(LocalDate.of(1981, 11, 16))
                .build();
    }

    @Test
    void addFilmTest() throws ValidationException {
        filmDbStorage.addFilm(film);
        assertThat(film).extracting("id").isNotNull();
        assertThat(film).extracting("name").isNotNull();
    }

    @Test
    void updateFilmTest() throws ValidationException {
        filmDbStorage.addFilm(film);
        film.setName("testUpdateFilm");
        film.setDescription("testUpdateDescription");
        filmDbStorage.updateFilm(film);
        assertThat(filmDbStorage.getFilm(film.getId()))
                .hasFieldOrPropertyWithValue("name", "testUpdateFilm")
                .hasFieldOrPropertyWithValue("description", "testUpdateDescription");
    }

    @Test
    void getFilmTest() throws ValidationException {
        filmDbStorage.addFilm(film);
        filmDbStorage.getFilm(film.getId());
        assertThat(filmDbStorage.getFilm(film.getId())).hasFieldOrPropertyWithValue("id", film.getId());
    }

    @Test
    void removeFilmTest() throws ValidationException {
        filmDbStorage.addFilm(film);
        filmDbStorage.removeFilm(film.getId());
        Assertions.assertThatThrownBy(() -> filmDbStorage.getFilm(film.getId()))
                .isInstanceOf(FilmNotFoundException.class);
    }

    @Test
    void updateFilmNotFoundTest() {
        Assertions.assertThatThrownBy(() -> filmDbStorage.updateFilm(filmForUpdate))
                .isInstanceOf(FilmNotFoundException.class);
    }

    @Test
    void addLikeFilmTest() throws ValidationException {
        userDbStorage.addUser(user);
        filmDbStorage.addFilm(film);
        filmDbStorage.addLike(film.getId(), user.getId());
        assertThat(filmDbStorage.getListOfPopularFilms(film.getId()).isEmpty());
        assertThat(filmDbStorage.getListOfPopularFilms(film.getId())).isNotNull();
        assertThat(filmDbStorage.getListOfPopularFilms(film.getId()).size() == 2);
    }

    @Test
    void removeFilmLikeTest() throws ValidationException {
        userDbStorage.addUser(user);
        filmDbStorage.addFilm(film);
        filmDbStorage.addFilm(film);
        filmDbStorage.addLike(film.getId(), user.getId());
        filmDbStorage.removeLike(film.getId(), user.getId());
        assertThat(filmDbStorage.getListOfPopularFilms(film.getId()).isEmpty());
        assertThat(filmDbStorage.getListOfPopularFilms(film.getId())).isNotNull();
        assertThat(filmDbStorage.getListOfPopularFilms(film.getId()).size() == 1);
    }
}*/