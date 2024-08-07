package ru.yandex.practicum.filmorate.dal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Collection;

@Slf4j
@Repository
public class MpaRepository extends BaseRepository<Mpa> {
    private static final String SELECT_All_MPA = "SELECT * FROM mpa";
    private static final String SELECT_MPA = "SELECT * FROM mpa WHERE mpa_id = ?";

    public MpaRepository(JdbcTemplate jdbc, RowMapper<Mpa> mapper) {
        super(jdbc, mapper, Mpa.class);
    }

    public Collection<Mpa> getAll() {
        return findMany(SELECT_All_MPA);
    }

    public Mpa getById(Integer id) {
        return findOne(SELECT_MPA, id).orElseThrow(() -> new NotFoundException("Mpa by ID = " + id + " not found"));
    }
}

