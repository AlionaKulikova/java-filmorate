package ru.yandex.practicum.filmorate.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice()
public class MainExceptionHandler {
    @ExceptionHandler(ConditionsNotMetException.class)
    public ResponseEntity<?> entityNotFound(final ConditionsNotMetException e) {
        return new ResponseEntity<>(new ErrorResponse(String.valueOf(e.getClass()), e.getMessage()), HttpStatus.valueOf(400));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> validationException(final NotFoundException e) {
        return new ResponseEntity<>(new ErrorResponse(String.valueOf(e.getClass()), e.getMessage()), HttpStatus.valueOf(404));
    }
}