package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.*;
import java.time.LocalDate;

//Анотация @Builder нздесь не хочет работать, наверное из-за того что в ручную написаны конструкторы,
//по другому не получилось из-за поля name = login, пробовал делать это присвоение делать в классе Validations,
//но в этом случае переставало все работать.  )
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class User {
    private int id;
    @NonNull
    @NotEmpty
    @Email(regexp = "[A-Za-z0-9._%-+]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}", message = "Пожалуйста укажите дейтвительный адрес")
    private String email;
    @NonNull
    @NotBlank
    private String login;
    private String name;
    @Past
    private LocalDate birthday;

    /*public User(String login, String name, String email, LocalDate birthday){
        this.login = login;
        this.email = email;
        this.birthday = birthday;
        this.name = name;
    }

    public User(String login, String email, LocalDate birthday){
        this.login = login;
        this.email = email;
        this.birthday = birthday;
        this.name = login;
    }
    public User(){
    }*/
}
