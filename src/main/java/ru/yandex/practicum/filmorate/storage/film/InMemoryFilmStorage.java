package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private static final Map<Long, Film> films = new HashMap<>();

    @Override
    public Collection<Film> getAllFilms() {
        log.info("Получение фильмов");
        return films.values();
    }

    @Override
    public Film getFilmById(Long id) {
        if (films.containsKey(id)) {
            log.info("Получили фильм c id " + id);

            return films.get(id);
        } else {
            log.error("Фильм не получен c id " + id);

            throw new NotFoundException("Фильм не найден с id " + id);
        }
    }

    @Override
    public Film saveFilm(Film film) {
        films.put(film.getId(), film);
        log.info("Фильм успешно создан с ID: {}", film.getId());

        return film;
    }

    @Override
    public Film updateFilm(Film newFilm) {
        if (films.containsKey(newFilm.getId())) {
            return films.get(newFilm.getId());
        }

        log.error("Ошибка при обновлении: Фильм с id = {} не найден ", newFilm.getId());
        throw new NotFoundException("Фильм с id = " + newFilm.getId() + " не найден");
    }
}
