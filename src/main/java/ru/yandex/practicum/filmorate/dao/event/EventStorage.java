package ru.yandex.practicum.filmorate.dao.event;

import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.EventEnum.OperationType;

import java.util.List;

public interface EventStorage {
    Event addLike(int user_id, OperationType operation, int film_id);
    Event addReview(int user_id, OperationType operation, int review_id);
    Event addFriend(int user_id, OperationType operation, int friend_id);

    Event add(Event feed);

    List<Event> get(int user_id);
}
