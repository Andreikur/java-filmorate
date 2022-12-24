package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.model.EventEnum.EventType;
import ru.yandex.practicum.filmorate.model.EventEnum.OperationType;

import javax.validation.constraints.Positive;

@Data
@Builder
public class Event {

    private Long timestamp;
    @Positive
    private int userId;
    private EventType eventType;
    private OperationType operation;
    @Positive
    private int eventId;
    @Positive
    private int entityId;
}
