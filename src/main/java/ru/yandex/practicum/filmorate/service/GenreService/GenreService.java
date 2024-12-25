package ru.yandex.practicum.filmorate.service.GenreService;

import ru.yandex.practicum.filmorate.dto.GenreDto;

import java.util.List;

public interface GenreService {
    GenreDto readById(Long id);

    List<GenreDto> readAll();
}
