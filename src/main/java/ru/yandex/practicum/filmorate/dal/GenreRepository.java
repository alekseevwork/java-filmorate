package ru.yandex.practicum.filmorate.dal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;

@Slf4j
@Repository
public class GenreRepository extends BaseRepository<Genre> {

    private static final String SELECT_ALL_GENRE = "SELECT * FROM genre";
    private static final String SELECT_GENRE = "SELECT * FROM genre WHERE genre_id = ?";

    public GenreRepository(JdbcTemplate jdbc, RowMapper<Genre> mapper) {
        super(jdbc, mapper, Genre.class);
    }

    public Collection<Genre> getAll() {
        return findMany(SELECT_ALL_GENRE);
    }

    public Genre getById(Integer id) {
        return findOne(SELECT_GENRE, id).orElseThrow(() -> new NotFoundException("Genre by ID = " + id + " not found"));
    }

}
