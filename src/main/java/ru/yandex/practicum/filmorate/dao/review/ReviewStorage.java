package ru.yandex.practicum.filmorate.dao.review;

import ru.yandex.practicum.filmorate.model.Review;
import java.util.List;

public interface ReviewStorage {

    /**
     * получить отзыв по его id
     * @param reviewId id отзыва
     * @return объект типа Review или null если не найдено
     */
    Review getReviewById(int reviewId);

    /**
     * получить список отзывов к фильму, отсортированный по убыванию полезности
     * @param filmId id фильма, если указано 0 то выдать отзывы без фильтрации по film_id
     * @param count количество отзывов, если указано 0 то выдать 10 отзывов
     * @return список объектов типа Review
     */
    List<Review> getReviewByFilmId(int filmId, int count);

    /**
     * добавить новый отзыв
     * @param review объект Review с заполненными полями (кроме id)
     * @return объект Review с заполненными полями
     */
    Review addReview(Review review);

    /**
     * изменить существующий отзыв
     * @param review объект Review с заполненными полями
     * @return обновленный объект Review или null если объект с таким id не найден в БД
     */
    Review updateReview(Review review);

    /**
     * удалить отзыв
     * @param reviewId id записи данных отзыва в БД
     */
    void deleteReview(int reviewId);

    /**
     * пользователь ставит лайк отзыву
     * @param reviewId id отзыва в БД
     * @param userId id пользователя
     */
    void likeReview(int reviewId, int userId);

    /**
     * пользователь ставит дизлайк отзыву
     * @param reviewId id отзыва в БД
     * @param userId id пользователя
     */
    void dislikeReview(int reviewId, int userId);

    /**
     * пользователь удаляет свой предыдущий лайк/дизлайк
     * @param reviewId id записи данных отзыва в БД
     * @param userId id пользователя
     */
    void deleteLike(int reviewId, int userId);
}
