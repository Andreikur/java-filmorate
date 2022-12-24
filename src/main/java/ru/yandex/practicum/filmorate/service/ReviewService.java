package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.event.EventStorage;
import ru.yandex.practicum.filmorate.dao.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.dao.film.FilmStorage;
import ru.yandex.practicum.filmorate.dao.review.ReviewDbStorage;
import ru.yandex.practicum.filmorate.dao.review.ReviewStorage;
import ru.yandex.practicum.filmorate.dao.user.UserDbStorage;
import ru.yandex.practicum.filmorate.model.EventEnum.OperationType;
import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewStorage reviewDbStorage;
    private final UserDbStorage userDbStorage;
    private final FilmDbStorage filmDbStorage;
    private final EventStorage eventStorage;

    public Review addReview(Review review) {
        eventStorage.addReview(review.getUserId(), OperationType.ADD, review.getFilmId());
        return reviewDbStorage.addReview(review);
    }

    public Review updateReview(Review review) {
        eventStorage.addReview(review.getUserId(), OperationType.UPDATE, review.getFilmId());
        return reviewDbStorage.updateReview(review);
    }

    public void deleteReview(int reviewId) {
        eventStorage.addReview(reviewDbStorage.getReviewById(reviewId).getUserId(), OperationType.REMOVE, reviewDbStorage.getReviewById(reviewId).getFilmId());
        reviewDbStorage.deleteReview(reviewId);

    }

    public Review getReviewById(int reviewId) {
        return reviewDbStorage.getReviewById(reviewId);
    }

    public List<Review> getReviewByFilmId(int filmId, int count) {
        return reviewDbStorage.getReviewByFilmId(filmId, count);
    }

    public void likeReview(int reviewId, int userId) {
        reviewDbStorage.likeReview(reviewId, userId);
    }

    public void dislikeReview(int reviewId, int userId) {
        reviewDbStorage.dislikeReview(reviewId, userId);
    }

    public void deleteLike(int reviewId, int userId) {
        reviewDbStorage.deleteLike(reviewId, userId);
    }
}