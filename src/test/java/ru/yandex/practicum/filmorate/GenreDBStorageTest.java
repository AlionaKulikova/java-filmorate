package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.dal.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:schema.sql")
@SpringBootTest
public class GenreDBStorageTest {
    private final FilmDbStorage filmDBStorage;
    private final GenreDBStorage genreDBStorage;

    @Test
    public void readAllGenresTest() {
        Assertions.assertEquals(6, genreDBStorage.readAll().size(), "Неверное количество жанров");
    }

    @Test
    public void getGenreByIdTest() {
        Genre genre = genreDBStorage.readById(1L);
        Assertions.assertEquals("Комедия", genre.getName(), "Неверное имя жанра.");
    }

    @Test
    public void getGenreByFilmIDTest() {
        Film film = Film.builder()
                .id(1L)
                .name("TestName")
                .description("TestDescription")
                .releaseDate(LocalDate.of(2005, 7, 1))
                .duration(100)
                .genres(Set.of(new Genre(3L, "Мультфильм")))
                .likes(new HashSet<>())
                .mpa(new Mpa(4L, "PG-13"))
                .build();
        filmDBStorage.saveFilm(film);

        List<Genre> genres = new ArrayList<>(genreDBStorage.getGenresByFilmID(1L));
        Assertions.assertEquals("Мультфильм", genres.get(0).getName(), "Неверный жанр у фильма.");
    }
}
