package ru.yandex.practicum.filmorate.dao.event;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.EventEnum.EventType;
import ru.yandex.practicum.filmorate.model.EventEnum.OperationType;
import lombok.extern.slf4j.Slf4j;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import static ru.yandex.practicum.filmorate.model.EventEnum.EventType.*;


@Component
@Slf4j
@RequiredArgsConstructor
public class EventDbStorageImpl implements EventStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Event addLike(int userId, OperationType operation, int filmId) {
        return add(Event.builder()
                .timestamp(new Timestamp(System.currentTimeMillis()).getTime())
                .userId(userId)
                .eventType(LIKE)
                .operation(operation)
                .entityId(filmId)
                .build());
    }
    @Override
    public Event addReview(int userId, OperationType operation, int reviewId) {

        final String checkQuery = "select * from USERS where USER_ID=?";
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(checkQuery, userId);
        if (!userRows.next()) {
            log.info("Пользователь не найден");
            throw new UserNotFoundException(String.format(
                    "Пользователь %s не найден", userId));
        }

        return add(Event.builder()
                .timestamp(new Timestamp(System.currentTimeMillis()).getTime())
                .userId(userId)
                .eventType(REVIEW)
                .operation(operation)
                .entityId(reviewId)
                .build());
    }
    @Override
    public Event addFriend(int userId, OperationType operation, int friendId) {
        return add(Event.builder()
                .timestamp(new Timestamp(System.currentTimeMillis()).getTime())
                .userId(userId)
                .eventType(FRIEND)
                .operation(operation)
                .entityId(friendId)
                .build());
    }
    @Override
    public Event add(Event event) {
        final String sql = "INSERT INTO EVENTS (event_time, user_id, event_type, operation, entity_id ) "
                + "VALUES ( ?, ?, ?, ?, ? )";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sql, new String[]{"event_id"});
            stmt.setTimestamp(1, new Timestamp(event.getTimestamp()));
            stmt.setLong(2, event.getUserId());
            stmt.setString(3, event.getEventType().toString());
            stmt.setString(4, event.getOperation().toString());
            stmt.setLong(5, event.getEntityId());
            return stmt;
        }, keyHolder);
        System.out.println("+++++++"+event);
        return event;
    }
    @Override
    public List<Event> get(int userId) {
        ///////////////////////////////////
        final String checkQuery = "select * from USERS where USER_ID=?";
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(checkQuery, userId);
        if (!userRows.next()) {
            log.info("Пользователь не найден");
            throw new UserNotFoundException(String.format(
                    "Пользователь %s не найден", userId));
        }
        //////////////////////////////////////////////
        final String sql = "SELECT * FROM EVENTS WHERE user_id = ? ORDER BY event_time ASC";
        return jdbcTemplate.query(sql, this::mapRowToFeed, userId);
    }

    private Event mapRowToFeed(ResultSet rs, int rowNum) throws SQLException {
        return Event.builder()
                .timestamp(rs.getTimestamp("event_time").getTime())
                .userId(rs.getInt("user_id"))
                .eventType(EventType.valueOf(rs.getString("event_type")))
                .operation(OperationType.valueOf(rs.getString("operation")))
                .eventId(rs.getInt("event_id"))
                .entityId(rs.getInt("entity_id"))
                .build();
    }
}
