package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NonNull;
import ru.yandex.practicum.filmorate.exception.ValidationException;

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

    public Film (String name, String description, LocalDate releaseDate, int duration){
        this.name = name;
        this.description = description;
        this.duration = duration;
        try {
            if (releaseDate.isAfter(LocalDate.parse("1895-12-28"))) {
                this.releaseDate = releaseDate;
            } else {
                throw  new ValidationException("Дата релиза не может быть раньше 28 декабря 1895");
            }
        } catch (ValidationException e){
            System.out.println(e.getMessage());
        }
    }

}
