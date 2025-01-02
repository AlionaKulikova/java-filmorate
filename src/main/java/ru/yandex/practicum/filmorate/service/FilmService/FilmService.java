package ru.yandex.practicum.filmorate.service.FilmService;

import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmService {
    Collection<FilmDto> findAllFilms();

    FilmDto findFilmById(Long id);

    FilmDto createFilm(Film film);

    FilmDto updateFilm(Film film);

    void addLikeFilm(Long idFilm, Long idUser);

    void deleteLikeFilm(Long idFilm, Long idUser);

    Collection<FilmDto> getPopularFilms(Long count);
}
