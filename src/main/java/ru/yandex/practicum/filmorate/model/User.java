package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
public class User {
    private int id;
    @NonNull
    @NotEmpty
    @Email(regexp = "[A-Za-z0-9._%-+]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}", message="Пожалуйста укажите дейтвительный адрес")
    private String email;
    @NonNull
    @NotBlank
    private String login;
    private String name;
    @Past
    private LocalDate birthday;
}
