package ru.yandex.practicum.filmorate.service.FilmService;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmService {
    Collection<Film> findAllFilms();

    Film findFilmById(Long id);

    Film createFilm(Film film);

    Film updateFilm(Film film);

    void addLikeFilm(Long idFilm, Long idUser);

    void deleteLikeFilm(Long idFilm, Long idUser);

    Collection<Film> getPopularFilms(Long count);
}
