package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.time.LocalDate;
import java.util.Objects;

@Data
public class User {

    private int id;
    @NonNull
    @NotBlank
    @Email
    private String email;
    @NonNull
    @NotBlank
    private String login;
    private String name;
    @Past
    private LocalDate birthday;

    public User( String email, String login, LocalDate birthday){
        this.email = email;
        this.login = login;
        this.birthday = birthday;
        this.name = login;
    }

    public User( String email, String login, String name, LocalDate birthday){
        this.email = email;
        this.login = login;
        this.birthday = birthday;
        if (name.isBlank()){
            this.name = login;
        } else {
            this.name = login;
        }
    }
}
