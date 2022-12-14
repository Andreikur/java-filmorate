package ru.yandex.practicum.filmorate.dao.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.event.EventStorage;
import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.model.EventEnum.OperationType;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.validations.Validations;

import java.sql.*;
import java.time.LocalDate;
import java.util.List;

@Component
@Slf4j
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private final EventStorage eventStorage;

    public FilmDbStorage(JdbcTemplate jdbcTemplate, EventStorage eventStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.eventStorage = eventStorage;
    }

    @Override
    public Film addFilm(Film film) throws ValidationException {
        Validations.validateFilm(film);
        String sglQuery = "insert into FILMS (FILM_NAME, DESCRIPTION, RELEASE_DATE, DURATION) values (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            PreparedStatement stmt = con.prepareStatement(sglQuery, new String[]{"FILM_ID"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            final LocalDate releaseDate = film.getReleaseDate();
            if (releaseDate == null) {
                stmt.setNull(3, Types.DATE);
            } else {
                stmt.setDate(3, Date.valueOf(releaseDate));
            }
            stmt.setInt(4, film.getDuration());
            return stmt;
        }, keyHolder);
        film.setId(keyHolder.getKey().intValue());

        final String mpaSqlQuery = "insert into FILM_MPA (FILM_ID, MPA_ID) VALUES (?, ?)";
        jdbcTemplate.update(mpaSqlQuery, film.getId(), film.getMpa().getId());

        final String genresSqlQuery = "insert into FILM_GENRE (FILM_ID, GENRE_ID) VALUES (?, ?)";
        if (film.getGenres() != null) {
            for (Genre genre : film.getGenres()) {
                jdbcTemplate.update(genresSqlQuery, film.getId(), genre.getId());
            }
        }
        film.setMpa(findMpa(film.getId()));
        film.setGenres(findGenres(film.getId()));
        return film;
    }

    @Override
    public Film updateFilm(Film film) throws ValidationException {
        Validations.validateFilm(film);
        final String checkQuery = "select * from FILMS where FILM_ID=?";
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(checkQuery, film.getId());
        if (!userRows.next()) {
            log.info("?????????? ???? ????????????????");
            throw new FilmNotFoundException(String.format(
                    "??????????  %s ???? ????????????", film.getName()));
        }
        final String sglQuery = "update FILMS set FILM_NAME=?, DESCRIPTION=?, RELEASE_DATE=?, DURATION=?" +
                "where FILM_ID=?";
        jdbcTemplate.update(sglQuery, film.getName(), film.getDescription(), film.getReleaseDate(),
                film.getDuration(), film.getId());
        final String sglQueryMpa = "update FILM_MPA set MPA_ID=? where FILM_ID=?";
        jdbcTemplate.update(sglQueryMpa, film.getMpa().getId(), film.getId());

        final String sglQueryGenreRemove = "delete from FILM_GENRE where FILM_ID=?";
        jdbcTemplate.update(sglQueryGenreRemove, film.getId());

        final String sglQueryGenre = "insert into FILM_GENRE (FILM_ID, GENRE_ID) values ( ?, ?)";
        final String checkQueryDuplicate = "select * from FILM_GENRE where FILM_ID=? and GENRE_ID=?";
        if (film.getGenres() != null) {
            for (Genre genre : film.getGenres()) {
                SqlRowSet genreRowsDuplicate = jdbcTemplate.queryForRowSet(checkQueryDuplicate, film.getId(), genre.getId());
                if (!genreRowsDuplicate.next()) {
                    jdbcTemplate.update(sglQueryGenre, film.getId(), genre.getId());
                }
            }
        }
        log.info("?????????? ????????????????");
        film = getFilm(film.getId());
        return film;
    }

    @Override
    public List<Film> getAllFilms() {
        final String sqlQuery = "select * from FILMS";
        return jdbcTemplate.query(sqlQuery, this::makeFilm);
    }

    @Override
    public Film getFilm(int id) {
        final String checkQuery = "select * from FILMS where FILM_ID=?";
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(checkQuery, id);
        if (!userRows.next()) {
            log.info("?????????? ???? ????????????");
            throw new FilmNotFoundException(String.format(
                    "?????????? %s ???? ????????????", id));
        }
        final List<Film> films = jdbcTemplate.query(checkQuery, this::makeFilm, id);
        if (films.isEmpty()) {
            log.info("???????????????????????? ???? ??????????????");
            return null;
        }
        return films.get(0);
    }

    @Override
    public List<Film> getListOfPopularFilms(int count) {

        final String sqlQuery = "select * from FILMS " +
                "left join USER_LIKED_FILM ULF ON FILMS.FILM_ID = ULF.FILM_ID " +
                "group by FILMS.FILM_ID, ULF.FILM_ID, ULF.USER_ID " +
                "order by COUNT(ULF.FILM_ID) " +
                "DESC LIMIT ?";
        return jdbcTemplate.query(sqlQuery, this::makeFilm, count);

    }

    public void addLike(int filmId, int userId) {
        final String checkFilmQuery = "select * from FILMS where FILM_ID=?";
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(checkFilmQuery, filmId);
        if (!filmRows.next()) {
            log.info("?????????? ???? ????????????");
            throw new FilmNotFoundException(String.format(
                    "?????????? %s ???? ????????????", filmId));
        }
        final String checkUsersQuery = "select * from USERS where USER_ID=?";
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(checkUsersQuery, userId);
        if (!userRows.next()) {
            log.info("???????????????????????? ???? ????????????");
            throw new UserNotFoundException(String.format(
                    "???????????????????????? %s ???? ????????????", userId));
        }
        final String sqlQuery = "insert into USER_LIKED_FILM (FILM_ID, USER_ID) VALUES (?, ?)";
        jdbcTemplate.update(sqlQuery, filmId, userId);
        eventStorage.addLike(userId, OperationType.ADD,filmId);
    }

    public void removeLike(int filmId, int userId) {
        final String checkFilmQuery = "select * from FILMS where FILM_ID=?";
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(checkFilmQuery, filmId);
        if (!filmRows.next()) {
            log.info("?????????? ???? ????????????");
            throw new FilmNotFoundException(String.format(
                    "?????????? %s ???? ????????????", filmId));
        }
        final String checkUsersQuery = "select * from USERS where USER_ID=?";
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(checkUsersQuery, userId);
        if (!userRows.next()) {
            log.info("???????????????????????? ???? ????????????");
            throw new UserNotFoundException(String.format(
                    "???????????????????????? %s ???? ????????????", userId));
        }
        final String sqlQuery = "delete from USER_LIKED_FILM where FILM_ID=? and USER_ID=?";
        jdbcTemplate.update(sqlQuery, filmId, userId);
        eventStorage.addLike(userId, OperationType.REMOVE,filmId);
    }

    public List<Mpa> getAllMpa() {
        final String checkQuery = "select * from MPA";
        return jdbcTemplate.query(checkQuery, this::makeMpa);
    }

    public Mpa getMpa(int id) {
        final String checkQuery = "select * from MPA where MPA_ID=?";
        SqlRowSet mpaRows = jdbcTemplate.queryForRowSet(checkQuery, id);
        if (!mpaRows.next()) {
            log.info("MPA ???? ????????????");
            throw new MpaNotFoundException(String.format(
                    "MPA %s ???? ????????????", id));
        }
        final List<Mpa> ratings = jdbcTemplate.query(checkQuery, this::makeMpa, id);
        if (ratings.isEmpty()) {
            log.info("MPA ???? ??????????????");
            return null;
        }
        return ratings.get(0);
    }

    public List<Genre> getAllGenre() {
        final String checkQuery = "select * from GENRE";
        return jdbcTemplate.query(checkQuery, this::makeGenre);
    }

    public Genre getGenre(int id) {
        final String checkQuery = "select * from GENRE where GENRE_ID=?";
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet(checkQuery, id);
        if (!genreRows.next()) {
            log.info("MPA ???? ????????????");
            throw new GenreNotFoundException(String.format(
                    "???????? %s ???? ????????????", id));
        }
        final List<Genre> genres = jdbcTemplate.query(checkQuery, this::makeGenre, id);
        if (genres.isEmpty()) {
            log.info("???????? ???? ??????????????");
            return null;
        }
        return genres.get(0);
    }

    private Film makeFilm(ResultSet rs, int rowNum) throws SQLException {
        Film film = new Film(rs.getInt("FILM_ID"),
                rs.getString("FILM_NAME"),
                rs.getString("DESCRIPTION"),
                rs.getDate("RELEASE_DATE").toLocalDate(),
                rs.getInt("DURATION")
        );

        final String sqlQueryMpa = "select * " +
                "from MPA " +
                "left join FILM_MPA as FM on MPA.MPA_ID= FM.MPA_ID " +
                "where FM.FILM_ID = ?";
        List<Mpa> mpa = jdbcTemplate.query(sqlQueryMpa, this::makeMpa, film.getId());
        film.setMpa(mpa.get(0));

        final String sqlQueryGenre = "select * " +
                "from GENRE " +
                "left join FILM_GENRE as FG on GENRE.GENRE_ID= FG.GENRE_ID " +
                "where FG.FILM_ID = ?";
        List<Genre> genreList = jdbcTemplate.query(sqlQueryGenre, this::makeGenre, film.getId());
        film.setGenres(genreList);

        return film;
    }

    private Mpa makeMpa(ResultSet rs, int rowNum) throws SQLException {
        return new Mpa(rs.getInt("MPA_ID"),
                rs.getString("MPA_NAME")
        );
    }

    private Genre makeGenre(ResultSet rs, int rowNum) throws SQLException {
        return new Genre(rs.getInt("GENRE_ID"),
                rs.getString("GENRE_NAME")
        );
    }

    private Mpa findMpa(int id) {
        final String mpaSqlQuery = "select * " +
                "from MPA " +
                "left join FILM_MPA as FM on MPA.MPA_ID= FM.MPA_ID " +
                "where FM.FILM_ID = ?";
        return jdbcTemplate.queryForObject(mpaSqlQuery, this::makeMpa, id);
    }

    private List<Genre> findGenres(int id) {
        final String genresSqlQuery = "select * " +
                "from GENRE " +
                "left join FILM_GENRE FG on GENRE.GENRE_ID = FG.GENRE_ID " +
                "where FG.FILM_ID = ?";
        return jdbcTemplate.query(genresSqlQuery, this::makeGenre, id);
    }

    @Override
    public void removeFilm(int id) {
        final String checkQuery = "select * from FILMS where FILM_ID=?";
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(checkQuery, id);
        if (!filmRows.next()) {
            log.info("?????????? ???? ????????????");
            throw new FilmNotFoundException(String.format(
                    "?????????? %s ???? ????????????", id));
        }
        // ???????????????? ???????????? ???? ????????????
        String sglQuery2 = "delete from USER_LIKED_FILM where FILM_ID=?";
        jdbcTemplate.update(sglQuery2, id);
        // ???????????????? ???????????? ???? ?????????????? FILM_GENRE
        String sglQuery3 = "delete from FILM_GENRE where FILM_ID=?";
        jdbcTemplate.update(sglQuery3, id);
        // ???????????????? ???????????? ???? ?????????????? FILM_MPA
        String sglQuery4 = "delete from FILM_MPA where FILM_ID=?";
        jdbcTemplate.update(sglQuery4, id);
        //???????????????? ????????????
        String sglQuery = "delete from FILMS where FILM_ID=?";
        jdbcTemplate.update(sglQuery, id);
    }
}
