package ru.yandex.practicum.filmorate.service.MpaService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.MpaStorage;
import ru.yandex.practicum.filmorate.dto.MpaDto;
import ru.yandex.practicum.filmorate.mapper.MpaMapper;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MpaServiceManager implements MpaService {
    private final MpaStorage mpaStorage;

    @Autowired
    public MpaServiceManager(MpaStorage mpaStorage) {
        this.mpaStorage = mpaStorage;
    }

    @Override
    public List<MpaDto> readAll() {
        log.info("Получение всего списка Mpa.");
        return mpaStorage.readAll()
                .stream()
                .map(MpaMapper::mapToMpaDto)
                .collect(Collectors.toList());
    }

    @Override
    public MpaDto readById(Long id) {
        log.info("Получение Mpa по id = {} ", id);
        return MpaMapper.mapToMpaDto(mpaStorage.readById(id));
    }
}
