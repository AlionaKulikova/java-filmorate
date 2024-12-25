/*package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService.FilmService;
import ru.yandex.practicum.filmorate.service.FilmService.FilmServiceManager;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FilmControllerTest {
    @Test
    void shouldSave() {
        Film film = Film.builder()
                .name("NameTestOne")
                .description("DescriptionTestOne")
                .releaseDate(LocalDate.of(2008, 1, 1))
                .duration(88888)
                .build();

        FilmStorage filmStorage = new InMemoryFilmStorage();
        UserStorage userStorage = new InMemoryUserStorage();
        FilmService filmService = new FilmServiceManager(filmStorage, userStorage);
        FilmController filmController = new FilmController(filmService);
        Film saveFilm = filmController.create(film);

        assertEquals(film, saveFilm);
    }

    @Test
    void shouldUpdate() {
        Film film = Film.builder()
                .name("NameTestTwo")
                .description("DescriptionTestTwo")
                .releaseDate(LocalDate.of(2007, 5, 7))
                .duration(88888)
                .build();

        FilmStorage filmStorage = new InMemoryFilmStorage();
        UserStorage userStorage = new InMemoryUserStorage();
        FilmService filmService = new FilmServiceManager(filmStorage, userStorage);
        FilmController filmController = new FilmController(filmService);
        Film saveFilm = filmController.create(film);

        saveFilm.setDescription("DescriptionTestThree");
        saveFilm.setDuration(55);
        saveFilm.setName("NameTestThree");
        saveFilm.getReleaseDate();

        Film updateFilm = filmController.create(saveFilm);

        assertEquals(saveFilm, updateFilm);
    }
}
*/