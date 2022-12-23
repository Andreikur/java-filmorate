package ru.yandex.practicum.filmorate.dao.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.relational.core.sql.In;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.director.DirectorDbStorage;
import ru.yandex.practicum.filmorate.dao.director.DirectorStorage;
import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.validations.Validations;

import java.sql.*;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
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

        final String directorSqlQuery = "insert into FILM_DIRECTORS (FILM_ID, DIRECTOR_ID) VALUES (?, ?)";
        if (film.getDirectors() != null) {
            for (Director director : film.getDirectors()) {
                jdbcTemplate.update(directorSqlQuery, film.getId(), director.getId());
            }
        }

        film.setMpa(findMpa(film.getId()));
        film.setGenres(findGenres(film.getId()));
        film.setDirectors(findDirector(film.getId()));
        return film;
    }

    @Override
    public Film updateFilm(Film film) throws ValidationException {
        Validations.validateFilm(film);
        final String checkQuery = "select * from FILMS where FILM_ID=?";
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(checkQuery, film.getId());
        if (!userRows.next()) {
            log.info("Фильм не обновлен");
            throw new FilmNotFoundException(String.format(
                    "Фильм  %s не найден", film.getName()));
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
        final String checkQueryDuplicateGenre = "select * from FILM_GENRE where FILM_ID=? and GENRE_ID=?";
        if (film.getGenres() != null) {
            for (Genre genre : film.getGenres()) {
                SqlRowSet genreRowsDuplicateGenre = jdbcTemplate.queryForRowSet(checkQueryDuplicateGenre, film.getId(), genre.getId());
                if (!genreRowsDuplicateGenre.next()) {
                    jdbcTemplate.update(sglQueryGenre, film.getId(), genre.getId());
                }
            }
        }

        final String sglQueryRemoveDirector = "delete from FILM_DIRECTORS where FILM_ID=?";
        jdbcTemplate.update(sglQueryRemoveDirector, film.getId());

        final String sglQueryDirector = "insert into FILM_DIRECTORS (FILM_ID, DIRECTOR_ID) values ( ?, ?)";
        if (film.getDirectors() != null) {
            for (Director director : film.getDirectors()) {
                jdbcTemplate.update(sglQueryDirector, film.getId(), director.getId());
            }
        }
        log.info("Фильм обновлен");
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
            log.info("Фильм не найден");
            throw new FilmNotFoundException(String.format(
                    "Фильм %s не найден", id));
        }
        final List<Film> films = jdbcTemplate.query(checkQuery, this::makeFilm, id);
        if (films.isEmpty()) {
            log.info("Пользователь не получен");
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
            log.info("Фильм не найден");
            throw new FilmNotFoundException(String.format(
                    "Фильм %s не найден", filmId));
        }
        final String checkUsersQuery = "select * from USERS where USER_ID=?";
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(checkUsersQuery, userId);
        if (!userRows.next()) {
            log.info("Пользователь не найден");
            throw new UserNotFoundException(String.format(
                    "Пользователь %s не найден", userId));
        }
        final String sqlQuery = "insert into USER_LIKED_FILM (FILM_ID, USER_ID) VALUES (?, ?)";
        jdbcTemplate.update(sqlQuery, filmId, userId);
    }

    public void removeLike(int filmId, int userId) {
        final String checkFilmQuery = "select * from FILMS where FILM_ID=?";
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(checkFilmQuery, filmId);
        if (!filmRows.next()) {
            log.info("Фильм не найден");
            throw new FilmNotFoundException(String.format(
                    "Фильм %s не найден", filmId));
        }
        final String checkUsersQuery = "select * from USERS where USER_ID=?";
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(checkUsersQuery, userId);
        if (!userRows.next()) {
            log.info("Пользователь не найден");
            throw new UserNotFoundException(String.format(
                    "Пользователь %s не найден", userId));
        }
        final String sqlQuery = "delete from USER_LIKED_FILM where FILM_ID=? and USER_ID=?";
        jdbcTemplate.update(sqlQuery, filmId, userId);
    }

    public List<Mpa> getAllMpa() {
        final String checkQuery = "select * from MPA";
        return jdbcTemplate.query(checkQuery, this::makeMpa);
    }

    public Mpa getMpa(int id) {
        final String checkQuery = "select * from MPA where MPA_ID=?";
        SqlRowSet mpaRows = jdbcTemplate.queryForRowSet(checkQuery, id);
        if (!mpaRows.next()) {
            log.info("MPA не найден");
            throw new MpaNotFoundException(String.format(
                    "MPA %s не найден", id));
        }
        final List<Mpa> ratings = jdbcTemplate.query(checkQuery, this::makeMpa, id);
        if (ratings.isEmpty()) {
            log.info("MPA не получен");
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
            log.info("MPA не найден");
            throw new GenreNotFoundException(String.format(
                    "Жанр %s не найден", id));
        }
        final List<Genre> genres = jdbcTemplate.query(checkQuery, this::makeGenre, id);
        if (genres.isEmpty()) {
            log.info("Жанр не получен");
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

        final String sqlQueryDirector = "select * " +
                "from DIRECTORS " +
                "left join FILM_DIRECTORS as FD on DIRECTORS.DIRECTOR_ID= FD.DIRECTOR_ID " +
                "where FD.FILM_ID = ?";
        List<Director> directorList = jdbcTemplate.query(sqlQueryDirector, this::makeDirector, film.getId());
        film.setDirectors(directorList);

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

    private Director makeDirector(ResultSet rs, int rowNum) throws SQLException {
        return new Director(rs.getInt("DIRECTOR_ID"),
                rs.getString("DIRECTOR_NAME")
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

    private List<Director> findDirector(int id) {
        final String genresSqlQuery = "select * " +
                "from DIRECTORS " +
                "left join FILM_DIRECTORS FD on DIRECTORS.DIRECTOR_ID = FD.DIRECTOR_ID " +
                "where FD.FILM_ID = ?";
        return jdbcTemplate.query(genresSqlQuery, this::makeDirector, id);
    }

    @Override
    public void removeFilm(int id) {
        final String checkQuery = "select * from FILMS where FILM_ID=?";
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(checkQuery, id);
        if (!filmRows.next()) {
            log.info("Фильм не найден");
            throw new FilmNotFoundException(String.format(
                    "Фильм %s не найден", id));
        }
        // удаление фильма из лайков
        String sglQuery2 = "delete from USER_LIKED_FILM where FILM_ID=?";
        jdbcTemplate.update(sglQuery2, id);
        // удаление фильма из таблици FILM_GENRE
        String sglQuery3 = "delete from FILM_GENRE where FILM_ID=?";
        jdbcTemplate.update(sglQuery3, id);
        // удаление фильма из таблици FILM_MPA
        String sglQuery4 = "delete from FILM_MPA where FILM_ID=?";
        jdbcTemplate.update(sglQuery4, id);
        //удаление фильма
        String sglQuery = "delete from FILMS where FILM_ID=?";
        jdbcTemplate.update(sglQuery, id);
    }

    /**

     * возвращает список общих фильмов 2 пользователей с сортировкой по популярности
     * @param userId id первого пользователя
     * @param friendId id друга первого пользователя
     * @return список объектов типа Film
     */
    @Override
    public List<Film> getCommonFilms(int userId, int friendId) {
        String sqlQuery = "SELECT film_id FROM user_liked_film WHERE user_id = ? " +
                    "AND film_id IN (SELECT film_id FROM user_liked_film WHERE user_id = ?)";
        List<Integer> commonFilmsIdList = jdbcTemplate.query(sqlQuery, (rs, rowNum) -> rs.getInt("film_id"), userId, friendId);

        if (!commonFilmsIdList.isEmpty()) {
            sqlQuery = "SELECT f.*, COUNT(ulf.film_id) AS cnt " +
                    "FROM films AS f LEFT JOIN user_liked_film AS ulf ON f.film_id = ulf.film_id " +
                    "WHERE f.film_id IN (" + commonFilmsIdList.stream().map(String::valueOf).collect(Collectors.joining(",")) + ") " +
                    "GROUP BY f.film_id " +
                    "ORDER BY cnt DESC";

            List<Film> commonFilmsList = jdbcTemplate.query(sqlQuery, this::makeFilm);

            return commonFilmsList;
        }
        else {
            return List.of();
        }
    }

     * отдать все фильмы определенного режиссёра с сортировкой по дате или по популярности
     * @param directorId id режиссера
     * @param sortBy сортировка если year то по дате, иначе по популярности (likes)
     * @return список объектов типа Film
     */
    public List<Film> getDirectorFilmList(int directorId, String sortBy) {
        //попробуем получить режиссера, если его не существует - будет выброшено DirectorNotFoundException
        Director director = new DirectorDbStorage(jdbcTemplate).getDirector(directorId);

        //получим список id всех фильмов режиссера
        String sqlQuery = "SELECT film_id FROM film_directors WHERE director_id = ?";
        List<Integer> filmIdList = jdbcTemplate.query(sqlQuery, (rs, rowNum) -> rs.getInt("film_id"), directorId);

        if (!filmIdList.isEmpty()) {
            if (sortBy != null && sortBy.equals("year")) {
                //сортировка по дате выпуска
                sqlQuery = "SELECT * " +
                        "FROM films " +
                        "WHERE film_id IN (" + filmIdList.stream().map(String::valueOf).collect(Collectors.joining(",")) +") " +
                        "ORDER BY release_date";
            }
            else {
                //сортировка по убыванию популярности
                sqlQuery = "SELECT f.*, COUNT(ulf.film_id) as cnt " +
                        "FROM films AS f LEFT JOIN user_liked_film AS ulf on f.film_id = ulf.film_id " +
                        "WHERE f.film_id IN (" + filmIdList.stream().map(String::valueOf).collect(Collectors.joining(",")) + ") " +
                        "GROUP BY f.film_id " +
                        "ORDER BY cnt DESC";
            }

            return jdbcTemplate.query(sqlQuery, this::makeFilm);
        }
        else {
            return List.of();
        }
    }
}
