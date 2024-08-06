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
    private static final String INSERT_QUERY = "INSERT INTO film (name, description, release_date, duration, mpa_id)" +
            "VALUES (?, ?, ?, ?, ?)";
    private static final String UPDATE_QUERY = "UPDATE film SET name = ?, description = ?, release_date = ?," +
            " duration = ?, mpa = ? WHERE id = ? VALUES(?, ?, ?, ?, ?, ?)";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM films WHERE id = ?";
    private static final String DELETE_BY_ID_QUERY = "DELETE FROM films WHERE id = ?";
    private static final String FIND_ALL_QUERY = "SELECT * FROM films";
    private static final String FIND_POPULAR_QUERY = "SELECT f.id, f.name, COUNT(fl.film_id) AS like_count\n" +
            "FROM film f\n" +
            "JOIN film_like fl ON f.id = fl.film_id\n" +
            "GROUP BY f.id, f.name\n" +
            "ORDER BY like_count DESC\n" +
            "LIMIT ?;";
    private static final String UPDATE_MPA_QUERY = "UPDATE film SET mpa = ? WHERE id = ? VALUES(?, ?)";
    private static final String INSERT_GENRE_QUERY = "INSERT INTO film_genre (genre_id, film_id) VALUES(?, ?)";

    public FilmRepository(JdbcTemplate jdbc, RowMapper<Film> mapper) {
        super(jdbc, mapper, Film.class);
    }

    @Override
    public Film create(Film film) {
        System.out.println("---------------");
        System.out.println(film);
        System.out.println(film.getName());
        System.out.println(film.getDescription());
        System.out.println(film.getReleaseDate());
        System.out.println(film.getDuration());
        System.out.println(film.getMpa().getId());
        Long filmId = insert(
                INSERT_QUERY,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId()
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