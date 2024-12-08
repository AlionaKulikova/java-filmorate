package ru.yandex.practicum.filmorate.service.FilmService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmServiceManager implements FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    static LocalDate minReleaseDate = LocalDate.of(1895, 12, 28);

    @Autowired
    public FilmServiceManager(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    @Override
    public Collection<Film> findAllFilms() {
        return filmStorage.getAllFilms();
    }

    @Override
    public Film findFilmById(Long filmId) {
        return filmStorage.getFilmById(filmId);
    }

    @Override
    public Film createFilm(Film film) {
        log.info("Создание фильма: {} ", film);
        validateFilm(film);
        film.setId(getNextId());

        return filmStorage.saveFilm(film);
    }

    @Override
    public Film updateFilm(Film newFilm) {
        log.info("Обновление фильма с ID: {} ", newFilm.getId());
        if (newFilm.getId() == null) {
            log.error("Ошибка при обновлении: Id должен быть указан");
            throw new ConditionsNotMetException("Id должен быть указан");
        }
        Film oldFilm = filmStorage.updateFilm(newFilm);
        validateFilm(newFilm);
        oldFilm.setName(newFilm.getName());
        oldFilm.setDescription(newFilm.getDescription());
        oldFilm.setReleaseDate(newFilm.getReleaseDate());
        oldFilm.setDuration(newFilm.getDuration());
        log.info("Фильм с ID: {} успешно обновлен ", newFilm.getId());

        return oldFilm;
    }

    @Override
    public void addLikeFilm(Long idFilm, Long idUser) {
        Film film = filmStorage.getFilmById(idFilm);
        userStorage.getUserById(idUser);
        film.getLikes().add(idUser);
        log.info("Лайк у фильма c id " + idFilm + " установлен от пользователя c id " + idUser);
    }

    @Override
    public void deleteLikeFilm(Long idFilm, Long idUser) {
        Film foundFilm = filmStorage.getFilmById(idFilm);
        userStorage.getUserById(idUser);
        foundFilm.getLikes().remove(idUser);
        log.info("Лайк у фильма c id " + idFilm + " удалён. Oт пользователя c id " + idUser);
    }

    @Override
    public Collection<Film> getPopularFilms(Long count) {
        log.info("Получение списка из " + count + " популярных фильмов.");
        return filmStorage.getAllFilms().stream()
                .sorted((a, b) -> b.getLikes().size() - a.getLikes().size())
                .limit(count)
                .collect(Collectors.toList());
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
       /* if (film.getGenres().size()<0) {
            log.error("Ошибка валидации: Жанр не может быть пустым");
            throw new ConditionsNotMetException("Жанр не может быть пустым");
        }
        if (film.getMpa() == null) {
            log.error("Ошибка валидации: MPA не может быть пустым");
            throw new ConditionsNotMetException("MPA не может быть пустым");
        }*/
    }

    private long getNextId() {
        long currentMaxId = filmStorage.getAllFilms().stream()
                .mapToLong(Film::getId) // Используем метод getId для получения ID фильмов
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}