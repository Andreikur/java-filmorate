package ru.yandex.practicum.filmorate.enums;

public enum FilmSearchOptions {
    DESCRIPTION,
    DIRECTOR,
    TITLE;

    static public boolean has(String value) {
        try {
            valueOf(value.toUpperCase());
            return true;
        } catch (IllegalArgumentException x) {
            return false;
        }
    }
}
