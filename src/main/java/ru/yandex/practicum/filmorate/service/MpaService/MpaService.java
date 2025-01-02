package ru.yandex.practicum.filmorate.service.MpaService;

import ru.yandex.practicum.filmorate.dto.MpaDto;

import java.util.List;

public interface MpaService {
    List<MpaDto> readAll();

    MpaDto readById(Long id);
}
