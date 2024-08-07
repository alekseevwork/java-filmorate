package ru.yandex.practicum.filmorate.dal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.Collection;
import java.util.List;

@Slf4j
@Repository
public class FilmRepository extends BaseRepository<Film> implements FilmService, FilmStorage {
    private static final String INSERT_FILM = "INSERT INTO film (name, description, release_date, duration, mpa_id)" +
            "VALUES (?, ?, ?, ?, ?)";
    private static final String UPDATE_FILM = "UPDATE film SET name = ?, description = ?, release_date = ?," +
            " duration = ?, mpa = ? WHERE film_id = ? VALUES(?, ?, ?, ?, ?, ?)";
    private static final String SELECT_BY_ID_FILM = "SELECT * FROM films WHERE film_id = ?";
    private static final String DELETE_BY_ID_FILM = "DELETE FROM films WHERE film_id = ?";
    private static final String SELECT_ALL_FILM = "SELECT * FROM films";
    private static final String SELECT_POPULAR_FILM = "SELECT f.id, f.name, COUNT(fl.film_id) AS like_count\n" +
            "FROM film f\n" +
            "JOIN film_like fl ON f.id = fl.film_id\n" +
            "GROUP BY f.id, f.name\n" +
            "ORDER BY like_count DESC\n" +
            "LIMIT ?;";
    private static final String UPDATE_MPA = "UPDATE film SET mpa = ? WHERE mpa_id = ? VALUES(?, ?)";
    private static final String INSERT_GENRE = "INSERT INTO film_genre (genre_id, film_id) VALUES(?, ?)";


    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private MpaRepository mpaRepository;

    public FilmRepository(JdbcTemplate jdbc, RowMapper<Film> mapper) {
        super(jdbc, mapper, Film.class);
    }

    @Override
    public FilmDto create(Film film) {
        System.out.println(film);
        FilmDto filmDto = FilmMapper.mapToFilmDto(film);

        mpaRepository.getById(film.getMpa().getId());
        System.out.println(genreRepository.getAll());
        if (!(film.getGenres() == null)) {
            System.out.println("genre not null");
            for (Genre genre: film.getGenres()) {
                System.out.println(genre);
                System.out.println(genreRepository.getById(genre.getId()));
            }
        }
        Long filmId = insert(
                INSERT_FILM,
                filmDto.getName(),
                filmDto.getDescription(),
                filmDto.getReleaseDate(),
                filmDto.getDuration(),
                filmDto.getMpa().getId()
                );
        film.setId(filmId);
        return filmDto;
    }

    @Override
    public Collection<Film> findAll() {
        return findMany(SELECT_ALL_FILM);
    }

    @Override
    public Film update(Film film) {
        if (findOne(SELECT_BY_ID_FILM, film.getId()).isEmpty()) {
            log.debug("Film update - Film = {}, not found", film);
            throw new NotFoundException("User not found");
        }
        update(
                UPDATE_FILM,
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
        delete(DELETE_BY_ID_FILM, id);
    }

    @Override
    public void addLike(Long filmId, Long userId) {

    }

    @Override
    public void deleteLike(Long filmId, Long userId) {

    }

    @Override
    public List<Film> getPopularLikesFilms(Integer sizeList) {
        return findMany(SELECT_POPULAR_FILM, sizeList);
    }
}
