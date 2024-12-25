package ru.yandex.practicum.filmorate.dal;

import java.util.Set;

public interface LikeStorage {
    Set<Long> getLikerByFilmId(Long filmID);

    void addLike(Long filmId, Long userId);

    void deleteLike(Long filmId, Long userId);
}
