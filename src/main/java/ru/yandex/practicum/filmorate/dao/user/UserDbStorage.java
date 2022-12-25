package ru.yandex.practicum.filmorate.dao.user;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.event.EventStorage;
import ru.yandex.practicum.filmorate.dao.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.validations.Validations;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

import static ru.yandex.practicum.filmorate.model.EventEnum.OperationType.ADD;
import static ru.yandex.practicum.filmorate.model.EventEnum.OperationType.REMOVE;

@Component
@Slf4j
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;
    private final EventStorage eventStorage;

    public UserDbStorage(JdbcTemplate jdbcTemplate, EventStorage eventStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.eventStorage = eventStorage;
    }

    private SqlRowSet checkUser(int id) {
        final String checkQuery = "select * from USERS where USER_ID=?";
        return jdbcTemplate.queryForRowSet(checkQuery, id);
    }

    /**
     * Создание пользователя
     */
    @Override
    public User addUser(User user) throws ValidationException {
        Validations.validateUser(user);
        String sglQuery = "insert into USERS (EMAIL, LOGIN, USER_NAME, BIRTHDAY) values (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement stmt = con.prepareStatement(sglQuery, new String[]{"USER_ID"});
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getLogin());
            stmt.setString(3, user.getName());
            final LocalDate birthday = user.getBirthday();
            if (birthday == null) {
                stmt.setNull(4, Types.DATE);
            } else {
                stmt.setDate(4, Date.valueOf(birthday));
            }
            return stmt;
        }, keyHolder);
        user.setId(keyHolder.getKey().intValue());
        return user;
    }

    /**
     * СОбновление пользователя
     */
    @Override
    public User updateUser(User user) throws ValidationException {
        Validations.validateUser(user);
        if (!checkUser(user.getId()).next()) {
            log.info("Пользователь не обновлен");
            throw new UserNotFoundException(String.format(
                    "Пользователь %s не найден", user.getName()));
        }
        final String sglQuery = "update USERS set EMAIL=?, LOGIN=?, USER_NAME=?, BIRTHDAY=? where USER_ID=?";
        jdbcTemplate.update(sglQuery, user.getEmail(), user.getLogin(), user.getName(), user.getBirthday(), user.getId());
        log.info("Пользователь обновлен");
        return user;
    }

    /**
     * Запрашиваем всех пользователе
     */
    @Override
    public List<User> getAllUsers() {
        final String sqlQuery = "select * from USERS";
        return jdbcTemplate.query(sqlQuery, UserDbStorage::makeUser);
    }

    /**
     * Запрашиваем пользователя по id
     */
    @Override
    public User getUser(int id) {
        if (!checkUser(id).next()) {
            log.info("Пользователь не найден");
            throw new UserNotFoundException(String.format(
                    "Пользователь %s не найден", id));
        }
        final String sqlQuery1 = "select USER_ID, EMAIL, LOGIN, USER_NAME, BIRTHDAY from USERS where USER_ID = ?";
        final List<User> users = jdbcTemplate.query(sqlQuery1, UserDbStorage::makeUser, id);
        if (users.isEmpty()) {
            log.info("Пользователь не получен");
            return null;
        }
        return users.get(0);
    }

    /**
     * Добавление в друзья
     */
    @Override
    public void addUserFiends(int id, int friendId) {
        String sglQuery1 = "insert into USER_FRIENDS (USER_ID, FRIEND_ID, FRIENDSHIP_CONFIRMED) values (?, ?, ?)";
        String sglQuery2 = "update USER_FRIENDS set FRIENDSHIP_CONFIRMED=? " +
                "where USER_ID=? and FRIEND_ID=? and FRIEND_ID<>USER_ID";
        if (!checkUser(id).next()) {
            log.info("Друг не добавлен");
            throw new UserNotFoundException(String.format("Пользователь %s не найден", id));
        }
        if (!checkUser(friendId).next()) {
            log.info("Друг не добавлен");
            throw new UserNotFoundException(String.format("Пользователь %s не найден", friendId));
        }
        final String checkMutualQuery = "select * from USER_FRIENDS  where USER_ID = ? AND FRIEND_ID = ?";
        SqlRowSet userRows3 = jdbcTemplate.queryForRowSet(checkMutualQuery, id, friendId);

        if (!userRows3.first()) {
            jdbcTemplate.update(sglQuery1, id, friendId, false);
        } else {
            jdbcTemplate.update(sglQuery2, true, id, friendId);
        }
        eventStorage.addFriend(id, ADD, friendId);
        log.info("Пользователи подружились");
    }

    /**
     * Удаление из друзей
     */
    @Override
    public void removeFriend(int id, int friendId) {
        if (!checkUser(id).next()) {
            log.info("Друг не удален");
            throw new UserNotFoundException(String.format("Пользователь %s не найден", id));
        }
        if (!checkUser(friendId).next()) {
            log.info("Друг не удален");
            throw new UserNotFoundException(String.format("Пользователь %s не найден", friendId));
        }
        final String sqlQuery = "delete from USER_FRIENDS where USER_ID=? and FRIEND_ID=?";
        jdbcTemplate.update(sqlQuery, id, friendId);
        eventStorage.addFriend(id,REMOVE, friendId);
    }

    /**
     * Запрашиваем всех друзей пользователя
     */
    @Override
    public List<User> findFriends(int id) {
        if (!checkUser(id).next()) {
            log.info("Пользователь не найден");
            throw new UserNotFoundException(String.format(
                    "Пользователь %s не найден", id));
        }
        final String sqlQuery = "select USERS.USER_ID, EMAIL, LOGIN, USER_NAME, BIRTHDAY " +
                "from USERS inner join USER_FRIENDS as UF on UF.FRIEND_ID = USERS.USER_ID " +
                "where UF.USER_ID=?";
        return jdbcTemplate.query(sqlQuery, UserDbStorage::makeUser, id);
    }

    /**
     * Запрашиваем список общих друзей
     */
    @Override
    public List<User> mutualFriends(int id, int otherId) {
        if (!checkUser(id).next()) {
            log.info("Пользователь не найден");
            throw new UserNotFoundException(String.format(
                    "Пользователь %s не найден", id));
        }
        if (!checkUser(otherId).next()) {
            log.info("Пользователь не найден");
            throw new UserNotFoundException(String.format(
                    "Пользователь %s не найден", otherId));
        }
        final String sqlQuery = "select USERS.USER_ID, EMAIL, LOGIN, USER_NAME, BIRTHDAY " +
                "from USERS inner join USER_FRIENDS as UF1 on UF1.FRIEND_ID = USERS.USER_ID" +
                " join USER_FRIENDS as UF2 on UF1.FRIEND_ID = UF2.FRIEND_ID " +
                "where UF1.USER_ID=? and UF2.USER_ID=?";
        return jdbcTemplate.query(sqlQuery, UserDbStorage::makeUser, id, otherId);
    }

    /**
     * Рекомендации фильмов другому пользователю по интересам данного пользователя
     *
     * @param id - идентификатор пользователя
     * @throws UserNotFoundException в случае, если пользователь не найден
     */
    @Override
    public List<Film> recommendations(int id) {
        if (!checkUser(id).next()) {
            log.info("Пользователь с id %s отсутствует в базе");
            throw new UserNotFoundException(String.format(
                    "Пользователь %s не найден", id));
        }
        List<Film> filmList = new ArrayList<>();
        final String sqlQuery = "select ULF.USER_ID " +
                "from USER_LIKED_FILM ULF " +
                "where ulf.FILM_ID in " +
                "                     (select FILM_ID " +
                "                      from USER_LIKED_FILM ulf1 " +
                "                      where USER_ID = ?) and ulf.USER_ID <> ?" +
                "group by ULF.USER_ID " +
                "order by COUNT(ULF.FILM_ID) desc " +
                "limit 1";
        final List<Integer> userIdList = jdbcTemplate.query(sqlQuery, UserDbStorage::makeUserIdRecommendations, id, id);
        if (userIdList.isEmpty()) {
            log.info("Пересечений по лайкам нет");
        } else {
            int userId = userIdList.get(0);
            //Сравнение предпочтений
            FilmDbStorage filmDbStorage = new FilmDbStorage(jdbcTemplate, eventStorage);
            final String FilmsSqlQuery1 = "select FILM_ID " +
                    "from USER_LIKED_FILM " +
                    "where USER_ID=?";
            List<Integer> filmListUser1 = jdbcTemplate.query(FilmsSqlQuery1, UserDbStorage::makeFilmIdRecommendations, id);
            final String FilmsSqlQuery2 = "select FILM_ID " +
                    "from USER_LIKED_FILM " +
                    "where USER_ID=?";
            List<Integer> filmListUser2 = jdbcTemplate.query(FilmsSqlQuery2, UserDbStorage::makeFilmIdRecommendations, userId);
            filmListUser2.removeAll(filmListUser1);
            for (int filId : filmListUser2) {
                filmList.add(filmDbStorage.getFilm(filId));
            }
        }
        return filmList;
    }

    static Integer makeUserIdRecommendations(ResultSet rs, int rowNum) throws SQLException {
        Integer userId = rs.getInt("USER_ID");
        return userId;
    }

    static Integer makeFilmIdRecommendations(ResultSet rs, int rowNum) throws SQLException {
        Integer userId = rs.getInt("FILM_ID");
        return userId;
    }

    static User makeUsersLikedFilm(ResultSet rs, int rowNum) throws SQLException {
        return new User(rs.getInt("USER_ID"),
                rs.getString("EMAIL"),
                rs.getString("LOGIN"),
                rs.getString("USER_NAME"),
                rs.getDate("BIRTHDAY").toLocalDate()
        );
    }

    static User makeUser(ResultSet rs, int rowNum) throws SQLException {
        return new User(rs.getInt("USER_ID"),
                rs.getString("EMAIL"),
                rs.getString("LOGIN"),
                rs.getString("USER_NAME"),
                rs.getDate("BIRTHDAY").toLocalDate()
        );
    }

    /**
     * Удаляем пользователя
     */
    @Override
    public void removeUser(int id) {
        if (!checkUser(id).next()) {
            log.info("Пользователь не найден");
            throw new UserNotFoundException(String.format(
                    "Пользователь %s не найден", id));
        }
        // удаление пользователя из списка друзей
        String sglQuery2 = "delete from USER_FRIENDS where USER_ID=? or FRIEND_ID=?";
        jdbcTemplate.update(sglQuery2, id, id);
        // удаление пользователя из списка USER_LIKED_FILM
        String sglQuery3 = "delete from USER_LIKED_FILM where USER_ID=?";
        jdbcTemplate.update(sglQuery3, id);
        //удаление пользователя
        String sglQuery = "delete from USERS where USER_ID=?";
        jdbcTemplate.update(sglQuery, id);
    }
}
