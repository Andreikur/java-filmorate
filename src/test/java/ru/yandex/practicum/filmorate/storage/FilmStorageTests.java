package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.dao.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.dao.user.UserDbStorage;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
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

    private final Film film = Film.builder()
            .name("filmName1")
            .description("description1")
            .releaseDate(LocalDate.of(2000, 12, 30))
            .duration(110)
            .mpa(new Mpa(1, "G"))
            .genres(null)
            .build();

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
        assertThat(film).hasFieldOrPropertyWithValue("id", film.getId());
    }

    @Test
    void updateFilmNotFoundTest() {
        Film filmForUpdate = Film.builder()
                .id(999)
                .name("film2")
                .description(("description2"))
                .releaseDate(LocalDate.of(2021, 3, 5))
                .mpa(new Mpa(1, "G"))
                .genres(null)
                .build();
        Assertions.assertThatThrownBy(() -> filmDbStorage.updateFilm(filmForUpdate))
                .isInstanceOf(FilmNotFoundException.class);
    }

    @Test
    void addLikeFilmTest() throws ValidationException {
        User user = User.builder()
                .id(1)
                .email("mail1@yandex.ru")
                .login("login1")
                .name("user1")
                .birthday(LocalDate.of(1981, 11, 16))
                .build();

        Film popularFilm = Film.builder()
                .name("testFilm")
                .description("description3")
                .releaseDate(LocalDate.of(2020, 12, 30))
                .duration(110)
                .mpa(new Mpa(1, "G"))
                .genres(null)
                .build();

        userDbStorage.addUser(user);
        filmDbStorage.addFilm(popularFilm);
        filmDbStorage.addLike(popularFilm.getId(), user.getId());
        assertThat(filmDbStorage.getListOfPopularFilms(popularFilm.getId()).isEmpty());
        assertThat(filmDbStorage.getListOfPopularFilms(popularFilm.getId())).isNotNull();
        assertThat(filmDbStorage.getListOfPopularFilms(popularFilm.getId()).size() == 2);
    }

    @Test
    void removeFilmLikeTest() throws ValidationException {
        User user1 = User.builder()
                .id(1)
                .email("mail2@yandex.ru")
                .login("login2")

                .name("mame2")
                .birthday(LocalDate.of(2000, 12, 22))
                .build();

        Film popularFilm = Film.builder()
                .name("testFilm")
                .description("description")
                .releaseDate(LocalDate.of(2020, 12, 30))
                .duration(110)
                .mpa(new Mpa(1, "G"))
                .genres(null)
                .build();

        userDbStorage.addUser(user1);
        filmDbStorage.addFilm(popularFilm);
        filmDbStorage.addFilm(popularFilm);
        filmDbStorage.addLike(popularFilm.getId(), user1.getId());
        filmDbStorage.removeLike(popularFilm.getId(), user1.getId());
        assertThat(filmDbStorage.getListOfPopularFilms(popularFilm.getId()).isEmpty());
        assertThat(filmDbStorage.getListOfPopularFilms(popularFilm.getId())).isNotNull();
        assertThat(filmDbStorage.getListOfPopularFilms(popularFilm.getId()).size() == 1);
    }
}
