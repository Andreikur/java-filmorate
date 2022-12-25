package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.review.ReviewStorage;
import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewStorage reviewDbStorage;

    public Review addReview(Review review) {
        return reviewDbStorage.addReview(review);
    }

    public Review updateReview(Review review) {
        return reviewDbStorage.updateReview(review);
    }

    public void deleteReview(int reviewId) {
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