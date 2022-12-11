package ru.yandex.practicum.filmorate.dao;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.UserFriends;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.validations.Validations;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

@Component
@Slf4j
@Qualifier("UserDbStorage")
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public User addUser(User user) throws ValidationException {
        Validations.validateUser(user);
        String sglQuery = "insert into USERS (EMAIL, LOGIN, USER_NAME, BIRTHDAY) values (?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement stmt = con.prepareStatement(sglQuery, new String[]{"USER_ID"});
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getLogin());
            //final String name = user.getName();
            //if ()
            stmt.setString(3, user.getName());
            final LocalDate birthday = user.getBirthday();
            if (birthday == null){
                stmt.setNull(4, Types.DATE);
            } else {
                stmt.setDate(4, Date.valueOf(birthday));
            }
            return stmt;
        }, keyHolder);
        user.setId(keyHolder.getKey().intValue());

        return user;
    }

    @Override
    public User updateUser(User user) throws ValidationException {
        Validations.validateUser(user);
        final String checkQuery = "select * from USERS where USER_ID=?";
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(checkQuery, user.getId());
        if (!userRows.next()){
            log.info("Пользователь не обновлен");
            throw new UserNotFoundException(String.format(
                    "Пользователь %s не найден",
                    user.getName()));
        }
        final String sglQuery = "update USERS set EMAIL=?, LOGIN=?, USER_NAME=?, BIRTHDAY=? where USER_ID=?";
        jdbcTemplate.update(sglQuery, user.getEmail(),user.getLogin(), user.getName(), user.getBirthday(), user.getId());
        log.info("Пользователь /% обновлен", user.getName());
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        final String sqlQuery = "select * from USERS";
        return jdbcTemplate.query(sqlQuery, UserDbStorage::makeUser);
    }

    @Override
    public User getUser(int id) {
        final String checkQuery = "select * from USERS where USER_ID=?";
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(checkQuery, id);
        if (!userRows.next()){
            log.info("Пользователь не найден");
            throw new UserNotFoundException(String.format(
                    "Пользователь %s не найден", id));
        }
        final String sqlQuery1 = "select USER_ID, EMAIL, LOGIN, USER_NAME, BIRTHDAY from USERS where USER_ID = ?";
        final List <User> users = jdbcTemplate.query(sqlQuery1, UserDbStorage::makeUser, id);
         if (users.isEmpty()){
             log.info("Пользователь не получен");
            return null;
        }
        return users.get(0);
    }

    @Override
    public void removeUser(int id) {

    }

    @Override
    public Map<Integer, User> getAllUsersMap() {
        Map<Integer, User> userMap = new HashMap<>();
        for (User user : getAllUsers()) {
            userMap.put(user.getId(), user);
        }
        return userMap;
    }

    @Override
    public void addUserFiends (int id, int friendId){
        String sglQuery1 = "insert into USER_FRIENDS (USER_ID, FRIEND_ID, FRIENDSHIP_CONFIRMED) values (?, ?, ?)";
        String sglQuery2 = "update USER_FRIENDS set FRIENDSHIP_CONFIRMED=? where USER_ID=? and FRIEND_ID=?";
        final String checkQuery = "select * from USERS where USER_ID=?";
        SqlRowSet userRows1 = jdbcTemplate.queryForRowSet(checkQuery, id);
        SqlRowSet userRows2 = jdbcTemplate.queryForRowSet(checkQuery, friendId);
        if (!userRows1.next()){
            log.info("Друг не добавлен");
            throw new UserNotFoundException(String.format("Пользователь %s не найден", id));
        }
        if (!userRows2.next()){
            log.info("Друг не добавлен");
            throw new UserNotFoundException(String.format("Пользователь %s не найден", friendId));
        }
        final String checkMutualQuery = "select * from USER_FRIENDS  where USER_ID = ? AND FRIEND_ID = ?";
        SqlRowSet userRows3 = jdbcTemplate.queryForRowSet(checkMutualQuery, id, friendId);
        if(!userRows3.first()){
            jdbcTemplate.update(sglQuery1, id, friendId, false);
        } else {
            jdbcTemplate.update(sglQuery2, true, id, friendId);
        }
        log.info("Пользователь %S подружился с пользователем %s", id, friendId);
    }

    @Override
    public void removeFriend(int id, int friendId){
        final String checkQuery = "select * from USERS where USER_ID=?";
        SqlRowSet userRows1 = jdbcTemplate.queryForRowSet(checkQuery, id);
        SqlRowSet userRows2 = jdbcTemplate.queryForRowSet(checkQuery, friendId);
        if (!userRows1.next()){
            log.info("Друг не удален");
            throw new UserNotFoundException(String.format("Пользователь %s не найден", id));
        }
        if (!userRows2.next()){
            log.info("Друг не удален");
            throw new UserNotFoundException(String.format("Пользователь %s не найден", friendId));
        }
        final String sqlQuery = "delete from  USER_FRIENDS where USER_ID=" + id + " and FRIEND_ID=" +friendId;
        jdbcTemplate.update(sqlQuery);

    }

    //вернуть всех друзей пользователя
    @Override
    public List<User> findFriends(int id){
        final String checkQuery = "select * from USER_FRIENDS where USER_ID=? or FRIEND_ID=?";
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(checkQuery, id, id);
        if (!userRows.next()){
            log.info("Пользователь не найден, или у него нет друзей");
            throw new UserNotFoundException(String.format(
                    "Пользователь %s не найден", id));
        }
        //final String sqlQuery = "select * from USERS inner join USER_FRIENDS as UF on USERS.USER_ID = UF.FRIEND_ID";
        final String sqlQuery = "select distinct USERS.USER_ID, EMAIL, LOGIN, USER_NAME, BIRTHDAY from USERS inner join USER_FRIENDS as UF on USERS.USER_ID = UF.FRIEND_ID";
        //final String sqlQuery = "select distinct USERS.USER_ID, EMAIL, LOGIN, USER_NAME, BIRTHDAY from USERS right join USER_FRIENDS as UF on  USERS.USER_ID = UF.USER_ID where USERS.USER_ID<>" + id;
        final List <User> users = jdbcTemplate.query(sqlQuery, UserDbStorage::makeUser);
        return users;
    }

    //список общих друзей
    public List<User> mutualFriends (int id, int otherId){
        final String checkQuery = "select * from USERS where USER_ID=?";
        SqlRowSet userRows1 = jdbcTemplate.queryForRowSet(checkQuery, id);
        SqlRowSet userRows2 = jdbcTemplate.queryForRowSet(checkQuery, otherId);
        if (!userRows1.next()){
            log.info("Пользователь не найден");
            throw new UserNotFoundException(String.format(
                    "Пользователь %s не найден", id));
        }
        if (!userRows2.next()){
            log.info("Пользователь не найден");
            throw new UserNotFoundException(String.format(
                    "Пользователь %s не найден", otherId));
        }
        final String sqlQuery = "select * from USERS inner join USER_FRIENDS as UF1 on USERS.USER_ID = UF1.FRIEND_ID inner join USER_FRIENDS as UF2 on  UF1.FRIEND_ID=  UF2.FRIEND_ID";
        return jdbcTemplate.query(sqlQuery, UserDbStorage::makeUser);
    }

    static User makeUser(ResultSet rs, int rowNum) throws SQLException {
        //final String sglQuery = "select FRIEND_ID from USER_FRIENDS where USER_ID=" + rowNum;

        return new User(rs.getInt("USER_ID"),
                rs.getString("EMAIL"),
                rs.getString("LOGIN"),
                rs. getString("USER_NAME"),
                rs.getDate("BIRTHDAY").toLocalDate()
                //new HashSet<>()
                //new UserFriends(rs.getInt("MPA.MPA_ID"), rs.getString("MPA.MPA"))
        );
    }
}
