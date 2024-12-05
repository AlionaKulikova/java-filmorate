package ru.yandex.practicum.filmorate.model;

import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
public class User {
    Long id;
    String email;
    String login;
    String name;
    LocalDate birthday;
    private final Set<Long> friends = new HashSet<>();
}
