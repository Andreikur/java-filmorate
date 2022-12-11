package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

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
    private int like;
    private Set<Integer> listOfUsersWhoHaveLiked = new HashSet<>();
    @NotNull
    private int genre;
    @NotNull
    private int ratingMPA;
}
