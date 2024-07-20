package ru.yandex.practicum.filmorate.exception;

public class DuplicatedMailException extends RuntimeException {
    public DuplicatedMailException(String message) {
        super(message);
    }
}