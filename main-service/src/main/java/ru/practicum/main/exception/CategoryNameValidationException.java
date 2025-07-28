package ru.practicum.main.exception;

public class CategoryNameValidationException extends RuntimeException {
    public CategoryNameValidationException(String message) {
        super(message);
    }
}
