package ru.yandex.practicum.filmorate.enums;

public enum FilmSearchOptions {
    DESCRIPTION,
    NAME;

    static public boolean has(String value) {
        try {
            valueOf(value.toUpperCase());
            return true;
        } catch (IllegalArgumentException x) {
            return false;
        }
    }
}
