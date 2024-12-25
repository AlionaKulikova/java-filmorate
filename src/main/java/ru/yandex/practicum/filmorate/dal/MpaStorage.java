package ru.yandex.practicum.filmorate.dal;

import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

public interface MpaStorage {
    List<Mpa> readAll();

    Mpa readById(Long id);
}
