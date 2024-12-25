package ru.yandex.practicum.filmorate.service.GenreService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.GenreStorage;
import ru.yandex.practicum.filmorate.dto.GenreDto;
import ru.yandex.practicum.filmorate.mapper.GenreMapper;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class GenreServiceManager implements GenreService {
    private final GenreStorage genreStorage;

    @Autowired
    public GenreServiceManager(GenreStorage genreStorage) {
        this.genreStorage = genreStorage;
    }

    @Override
    public List<GenreDto> readAll() {
        log.info("Получение всего списка жанров.");
        return genreStorage.readAll()
                .stream()
                .map(GenreMapper::mapToGenreDto)
                .collect(Collectors.toList());
    }

    @Override
    public GenreDto readById(Long id) {
        log.info("Получение жанра по id = {} ", id);
        return GenreMapper.mapToGenreDto(genreStorage.readById(id));
    }
}
