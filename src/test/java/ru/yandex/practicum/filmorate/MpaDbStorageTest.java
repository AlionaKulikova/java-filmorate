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
import java.util.HashSet;
import java.util.Set;

@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:schema.sql")
@SpringBootTest
public class MpaDbStorageTest {
    private final FilmDbStorage filmDBStorage;
    private final MpaDbStorage mpaDBStorage;

    @Test
    public void readAllMpaTest() {
        Assertions.assertEquals(5, mpaDBStorage.readAll().size(), "Неверное количество Mpa.");
    }

    @Test
    public void readMpaByIdTest() {
        Mpa mpa = new Mpa(1L, "G");
        Assertions.assertEquals(mpa, mpaDBStorage.readById(1L), "Получили неверный Mpa");
    }

    @Test
    public void readMpaByFilmIdTest() {
        Film film = Film.builder()
                .id(1L)
                .name("TestName")
                .description("TestDescription")
                .releaseDate(LocalDate.of(2005, 7, 1))
                .duration(100)
                .genres(Set.of(new Genre(3L, "Мультфильм")))
                .likes(new HashSet<>())
                .mpa(new Mpa(2L, "PG"))
                .build();
        filmDBStorage.saveFilm(film);
        Assertions.assertEquals(film.getMpa(), mpaDBStorage.readById(2L), "Неверный Mpa у фильма.");
    }
}
