package ru.yandex.practicum.filmorate.dao.director;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.DirectorNotFoundException;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Director;
import lombok.extern.slf4j.Slf4j;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class DirectorDbStorage implements DirectorStorage {

    private final JdbcTemplate jdbcTemplate;

    public Director addDirector(Director director) {
        String sglQuery = "insert into DIRECTORS (DIRECTOR_NAME) values (?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            PreparedStatement stmt = con.prepareStatement(sglQuery, new String[]{"DIRECTOR_ID"});
            stmt.setString(1, director.getName());
            return stmt;
        }, keyHolder);
        director.setId(keyHolder.getKey().intValue());
        return director;
    }

    public Director updateDirector(Director director) {
        final String checkQuery = "select * from DIRECTORS where DIRECTOR_ID=?";
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(checkQuery, director.getId());
        if (!userRows.next()) {
            log.info("Режисер не обновлен");
            throw new FilmNotFoundException(String.format(
                    "Режисер  %s не найден", director.getName()));
        }
        final String sglQuery = "update DIRECTORS set DIRECTOR_NAME=? " +
                "where DIRECTOR_ID=?";
        jdbcTemplate.update(sglQuery, director.getName(), director.getId());
        log.info("Режисер обновлен");
        director = getDirector(director.getId());
        return director;
    }

    public List<Director> getAllDirectors() {
        final String sqlQuery = "select * from DIRECTORS";
        return jdbcTemplate.query(sqlQuery, this::makeDirector);
    }

    public Director getDirector(int id) {
        final String checkQuery = "select * from DIRECTORS where DIRECTOR_ID=?";
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(checkQuery, id);
        if (!userRows.next()) {
            log.info("Режисер не найден");
            throw new DirectorNotFoundException(String.format(
                    "Режисер %s не найден", id));
        }
        final List<Director> directors = jdbcTemplate.query(checkQuery, this::makeDirector, id);
        if (directors.isEmpty()) {
            log.info("Режисер не получен");
            return null;
        }
        return directors.get(0);
    }

    public void removeDirector(int id) {
        final String checkQuery = "select * from DIRECTORS where DIRECTOR_ID=?";
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(checkQuery, id);
        if (!filmRows.next()) {
            log.info("Режисер не найден");
            throw new DirectorNotFoundException(String.format(
                    "Режисер  %s не найден", id));
        }
        // удаление режисера из фильмов
        String sglQuery2 = "delete from FILM_DIRECTORS where DIRECTOR_ID=?";
        jdbcTemplate.update(sglQuery2, id);
        //удаление режисера
        String sglQuery = "delete from DIRECTORS where DIRECTOR_ID=?";
        jdbcTemplate.update(sglQuery, id);
    }

    private Director makeDirector(ResultSet rs, int rowNum) throws SQLException {
        return new Director(rs.getInt("DIRECTOR_ID"),
                rs.getString("DIRECTOR_NAME")
        );
    }
}
