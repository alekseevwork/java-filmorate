package ru.yandex.practicum.filmorate.dal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

@Slf4j
@Repository
public class GenreRepository extends BaseRepository<Genre> {
    public GenreRepository(JdbcTemplate jdbc, RowMapper<Genre> mapper) {
        super(jdbc, mapper, Genre.class);
    }

    public List<Genre> getAll() {
        return findMany(sqlRequests.SELECT_ALL_GENRE);
    }

    public Genre getById(Long id) {
        return findOne(sqlRequests.SELECT_GENRE, id)
                .orElseThrow(() -> new NotFoundException("Genre by ID = " + id + " not found"));
    }

    public List<Genre> findGenresByFilmId(Long filmId) {
        return findMany(sqlRequests.SELECT_GENRES_BY_FILM_ID, filmId);
    }
}
