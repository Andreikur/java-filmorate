package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserFriends {
    private int id;
    //private Map<Integer, Boolean> list;
    private int friendsId;
    private boolean status;
}