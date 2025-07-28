package ru.practicum.main.exception;

public class UserEmailValidationException extends RuntimeException {
    public UserEmailValidationException(String message) {
        super(message);
    }
}
