package ru.yandex.practicum.filmorate.exception;

public class WrongSearchException extends RuntimeException {
    public WrongSearchException(String msg) {
        super(msg);
    }
}
