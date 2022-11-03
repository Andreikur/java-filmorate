package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
public class Film {

    private int id;
    @NonNull
    @NotBlank
    private String name;
    @Size(min = 0, max = 200)
    private String description;
    @Past
    private LocalDate releaseDate;
    @Positive
    private int duration;

}
