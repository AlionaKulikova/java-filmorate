package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.model.Film;

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

        Film saveFilm = FilmController.create(film);

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

        final Film saveFilm = FilmController.create(film);
        saveFilm.setDescription("DescriptionTestThree");
        saveFilm.setDuration(55);
        saveFilm.setName("NameTestThree");
        saveFilm.getReleaseDate();

        final Film updateFilm = FilmController.create(saveFilm);

        assertEquals(saveFilm, updateFilm);
    }
}
