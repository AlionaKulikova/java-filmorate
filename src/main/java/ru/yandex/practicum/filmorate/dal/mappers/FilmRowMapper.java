package ru.yandex.practicum.filmorate.dal.mappers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dal.*;
import ru.yandex.practicum.filmorate.model.Film;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class FilmRowMapper implements RowMapper<Film> {
    MpaStorage mpaDbStorage;
    LikeStorage likeDbStorage;
    GenreStorage genreDbStorage;

    @Autowired
    public FilmRowMapper(MpaStorage mpaDbStorage, LikeStorage likeDbStorage, GenreStorage genreDbStorage) {
        this.mpaDbStorage = mpaDbStorage;
        this.likeDbStorage = likeDbStorage;
        this.genreDbStorage = genreDbStorage;
    }

    @Override
    public Film mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return Film.builder()
                .id(resultSet.getLong("id"))
                .name(resultSet.getString("name"))
                .description(resultSet.getString("description"))
                .releaseDate(resultSet.getDate("releaseDate").toLocalDate())
                .duration(resultSet.getInt("duration"))
                .mpa(mpaDbStorage.readById(resultSet.getLong("mpaid")))
                .likes(likeDbStorage.getLikerByFilmId(resultSet.getLong("id")))
                .genres(genreDbStorage.getGenresByFilmID(resultSet.getLong("id")))
                .build();
    }
}