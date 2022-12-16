package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Map;

@Data
public class UserFriends {
    private int id;
    private int friendsId;
    private boolean status;
}