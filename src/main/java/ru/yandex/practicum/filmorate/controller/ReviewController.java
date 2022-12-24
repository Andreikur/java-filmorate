package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ReviewNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.service.ReviewService;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping(value = "/reviews")
@RestController
public class ReviewController {

    private final ReviewService reviewService;

    /**
     * добавление отзыва
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Review addReview(@Valid @RequestBody Review review) {
        return reviewService.addReview(review);
    }

    /**
     * обновление отзыва
     */
    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public Review updateReview(@Valid @RequestBody Review review) {
        return reviewService.updateReview(review);
    }

    /**
     * удаление отзыва по reviewId
     */
    @DeleteMapping("/{reviewId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteReview(@PathVariable int reviewId) {
        reviewService.deleteReview(reviewId);
    }

    /**
     * получить данные отзыва по reviewId
     */
    @GetMapping("/{reviewId}")
    @ResponseStatus(HttpStatus.OK)
    public Review getReviewById(@PathVariable int reviewId) {
        Review review = reviewService.getReviewById(reviewId);
        if (review == null) {
            throw new ReviewNotFoundException("Отзыв с id=" + reviewId + " не найден");
        }
        else {
            return review;
        }
    }

    /**
     * Получить отзывы по идентификатору фильма, если фильм не указан то все. Если кол-во не указано, то 10
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Review> getReviewByFilmId(@RequestParam(defaultValue = "0") int filmId
                                            , @RequestParam(defaultValue = "0") int count ) {

        return reviewService.getReviewByFilmId(filmId, count);
    }

    /**
     * поставить лайк к отзыву на фильм
     */
    @PutMapping("/{reviewId}/like/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void likeReview(@PathVariable int reviewId, @PathVariable int userId) {
        reviewService.likeReview(reviewId, userId);
    }

    /**
     * поставить дизлайк к отзыву на фильм
     */
    @PutMapping("/{reviewId}/dislike/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void dislikeReview(@PathVariable int reviewId, @PathVariable int userId) {
        reviewService.dislikeReview(reviewId, userId);
    }

    /**
     * удалить лайк или дизлайк к отзыву на фильм
     */
    @DeleteMapping({"/{reviewId}/like/{userId}", "/{reviewId}/dislike/{userId}"})
    @ResponseStatus(HttpStatus.OK)
    public void deleteLikeReview(@PathVariable int reviewId, @PathVariable int userId) {
        reviewService.deleteLike(reviewId, userId);
    }
}