package ru.yandex.practicum.filmorate.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorResponse {
    private String className;
    private String message;
}