package ru.yandex.practicum.filmorate.model;

import lombok.*;

@Data
public class UserFriends {
    private int id;
    private int friendsId;
    private boolean status;
}