package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {
    Collection<Film> getAllFilms();

    Film saveFilm(Film film);

    Film updateFilm(Film film);

    Film getFilmById(Long id);
}
