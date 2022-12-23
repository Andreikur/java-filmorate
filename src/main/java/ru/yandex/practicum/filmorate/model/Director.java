package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@Data
public class Director {
    private int id;
    @NotBlank
    private String name;
}