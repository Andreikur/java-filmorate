package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Film {
    private int id;
    @NotNull
    @NotBlank
    private String name;
    @Size(max = 200)
    private String description;
    @Past()
    private LocalDate releaseDate;
    @Positive
    private int duration;
    private Mpa mpa;
    private List<Genre> genres;

    public Film(int id, String name, String description, LocalDate releaseDate, int duration) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }
}