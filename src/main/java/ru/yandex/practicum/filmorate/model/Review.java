package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Review {
    /**
     * целочисленный идентификатор
     */
    private int reviewId;

    /**
     * текст отзыва
     */
    @NotBlank(message = "Текст отзыва не может быть пустым")
    private String content;

    /**
     * направленность отзыва: положительный/отрицательный
     */
    @NotNull
    private Boolean isPositive;

    /**
     * id пользователя, который оставил отзыв
     */
    @NotNull
    private Integer userId;

    /**
     * id фильма к которому оставлен отзыв
     */
    @NotNull
    private Integer filmId;

    /**
     * рейтинг полезности отзыва, не должен присваиваться напрямую - рассчитывается по имеющимся данным
     */
    private int useful;

    public Review(int reviewId, String content, boolean isPositive, int userId, int filmId) {
        this.reviewId = reviewId;
        this.content = content;
        this.isPositive = isPositive;
        this.userId = userId;
        this.filmId = filmId;
    }
}