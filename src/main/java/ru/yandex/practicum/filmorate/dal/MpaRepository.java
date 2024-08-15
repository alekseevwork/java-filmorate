package ru.yandex.practicum.filmorate.dal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dto.MpaDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.MpaMapper;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

@Slf4j
@Repository
public class MpaRepository extends BaseRepository<Mpa> {
    public MpaRepository(JdbcTemplate jdbc, RowMapper<Mpa> mapper) {
        super(jdbc, mapper, Mpa.class);
    }

    public List<MpaDto> getAll() {
        return findMany(SqlRequests.SELECT_All_MPA).stream().map(MpaMapper::mapToMpaDto).toList();
    }

    public Mpa getById(Long id) {
        return findOne(SqlRequests.SELECT_MPA, id).orElseThrow(() -> new NotFoundException("Mpa by ID = " + id + " not found"));
    }
}

