package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController

@RequestMapping("/films")
public class FilmController {
    static LocalDate minReleaseDate = LocalDate.of(1895, 12, 28);
    private static final Map<Long, Film> films = new HashMap<>();

    @GetMapping
    public Collection<Film> findAll() {
        return films.values();
    }

    @PostMapping
    public static Film create(@RequestBody Film film) {
        log.info("Создание фильма: {}", film);
        validateFilm(film);
        film.setId(getNextId());
        films.put(film.getId(), film);
        log.info("Фильм успешно создан с ID: {}", film.getId());
        return film;
    }

    @PutMapping
    public Film update(@RequestBody Film newFilm) {
        log.info("Обновление фильма с ID: {}", newFilm.getId());
        if (newFilm.getId() == null) {
            log.error("Ошибка при обновлении: Id должен быть указан");
            throw new ConditionsNotMetException("Id должен быть указан");
        }
        if (films.containsKey(newFilm.getId())) {
            Film oldFilm = films.get(newFilm.getId());
            validateFilm(newFilm);
            oldFilm.setName(newFilm.getName());
            oldFilm.setDescription(newFilm.getDescription());
            oldFilm.setReleaseDate(newFilm.getReleaseDate());
            oldFilm.setDuration(newFilm.getDuration());
            log.info("Фильм с ID: {} успешно обновлен", newFilm.getId());
            return oldFilm;
        }

        log.error("Ошибка при обновлении: Фильм с id = {} не найден", newFilm.getId());
        throw new NotFoundException("Фильм с id = " + newFilm.getId() + " не найден");
    }

    private static void validateFilm(Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            log.error("Ошибка валидации: Название не может быть пустым");
            throw new ConditionsNotMetException("Название не может быть пустым");
        }
        if (film.getDescription().length() > 200) {
            log.error("Ошибка валидации: Максимальная длина описания - 200 символов");
            throw new ConditionsNotMetException("Максимальная длина описания - 200 символов");
        }
        if (film.getReleaseDate().isBefore(minReleaseDate)) {
            log.error("Ошибка валидации: Дата выпуска не может быть раньше 28.12.1895 года");
            throw new ConditionsNotMetException("Дата выпуска не может быть раньше 28.12.1895 года");
        }
        if (film.getDuration() < 0) {
            log.error("Ошибка валидации: Длительность фильма не может быть отрицательной");
            throw new ConditionsNotMetException("Длительность фильма не может быть отрицательной");
        }
    }

    private static long getNextId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
