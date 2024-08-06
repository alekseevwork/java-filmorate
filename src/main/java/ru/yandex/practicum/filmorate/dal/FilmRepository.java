package ru.yandex.practicum.filmorate.dal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.Collection;
import java.util.List;

@Slf4j
@Repository
public class FilmRepository extends BaseRepository<Film> implements FilmService, FilmStorage {
    private static final String INSERT_QUERY = "INSERT INTO film (name, description, releaseDate, duration, mpa)" +
            "VALUES (?, ?, ?, ?, ?)";
    private static final String UPDATE_QUERY = "UPDATE film SET name = ?, description = ?, releaseDate = ?," +
            " duration = ?, mpa = ? WHERE id = ? VALUES(?, ?, ?, ?, ?, ?)";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM films WHERE id = ?";
    private static final String DELETE_BY_ID_QUERY = "DELETE FROM films WHERE id = ?";
    private static final String FIND_ALL_QUERY = "SELECT * FROM films";
    private static final String FIND_POPULAR_QUERY = "SELECT * FROM films " +
            "INNER JOIN film_like ON film.id = film_like.film_id GROUP BY COUNT( film_like.film_id) LIMIT ?";
    private static final String UPDATE_MPA_QUERY = "UPDATE film SET mpa = ? WHERE id = ? VALUES(?, ?)";
    private static final String INSERT_GENRE_QUERY = "INSERT INTO film_genre (genre_id, film_id) VALUES(?, ?)";

    public FilmRepository(JdbcTemplate jdbc, RowMapper<Film> mapper) {
        super(jdbc, mapper, Film.class);
    }

    @Override
    public Film create(Film film) {
        System.out.println(film);
        Long filmId = insert(
                INSERT_QUERY,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa()
                );
        film.setId(filmId);
        return film;
    }

    @Override
    public Collection<Film> findAll() {
        return findMany(FIND_ALL_QUERY);
    }

    @Override
    public Film update(Film film) {
        if (findOne(FIND_BY_ID_QUERY, film.getId()).isEmpty()) {
            log.debug("Film update - Film = {}, not found", film);
            throw new NotFoundException("User not found");
        }
        update(
                UPDATE_QUERY,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa(),
                film.getId()
                );
        return film;
    }

    public void deleteById(Long id) {
        delete(FIND_ALL_QUERY, id);
    }

    @Override
    public void addLike(Long filmId, Long userId) {

    }

    @Override
    public void deleteLike(Long filmId, Long userId) {

    }

    @Override
    public List<Film> getPopularLikesFilms(Integer sizeList) {
        return findMany(FIND_POPULAR_QUERY, sizeList);
    }
}
