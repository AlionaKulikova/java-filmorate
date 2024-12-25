package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Set;

public interface FilmStorage {
    Collection<Film> getAllFilms();

    Film getFilmById(Long id);

    Film saveFilm(Film film);

    Film updateFilm(Film film);

    public Set<Film> getTopFilms(Long count);
}
