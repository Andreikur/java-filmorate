package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@Getter
@Setter
public class Mpa {
    private int id;
    @NonNull
    @NotBlank
    private String name;
}
