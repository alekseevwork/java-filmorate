package ru.yandex.practicum.filmorate.dal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Repository
public class FilmRepository extends BaseRepository<Film> implements FilmService, FilmStorage {
    private final GenreRepository genreRepository;
    private final MpaRepository mpaRepository;

    @Autowired
    public FilmRepository(JdbcTemplate jdbc, RowMapper<Film> mapper, GenreRepository genreRepository, MpaRepository mpaRepository) {
        super(jdbc, mapper, Film.class);
        this.genreRepository = genreRepository;
        this.mpaRepository = mpaRepository;
    }

    @Override
    public FilmDto create(Film film) {
        validationById(film);

        FilmDto filmDto = FilmMapper.mapToFilmDto(film);
        filmDto.setMpa(mpaRepository.getById(film.getMpa().getId()));

        Long filmId = insert(
                sqlRequests.INSERT_FILM,
                filmDto.getName(),
                filmDto.getDescription(),
                filmDto.getReleaseDate(),
                filmDto.getDuration(),
                filmDto.getMpa().getId()
        );
        filmDto.setId(filmId);

        if (film.getGenres() != null) {
            for (Genre genre : film.getGenres()) {
                filmDto.getGenres().add(genreRepository.getById(genre.getId()));
                insertNotId(sqlRequests.INSERT_FILM_GENRE, genre.getId(), filmId);
            }
        }

        return filmDto;
    }

    @Override
    public Collection<Film> findAll() {

        List<Film> films = findMany(sqlRequests.SELECT_ALL_FILM);
        for (Film film : films) {
            film.setLikes(new HashSet<>(findManyInstances(sqlRequests.SELECT_LIKES_BY_ID_FILM, Long.class, film.getId())));
            film.setMpa(mpaRepository.getById(film.getMpa().getId()));
            film.setGenres(new HashSet<>(genreRepository.findGenresByFilmId(film.getId())));
        }
        return films.stream().map(FilmMapper::mapToFilmDto).collect(Collectors.toList());
    }

    public Film getFilmById(Long id) {
        Film film = findOne(sqlRequests.SELECT_BY_ID_FILM, id)
                .orElseThrow(() -> new NotFoundException("Фильм с ID = " + id + " не найден"));
        film.setLikes(new HashSet<>(findManyInstances(sqlRequests.SELECT_LIKES_BY_ID_FILM, Long.class, film.getId())));
        film.setMpa(mpaRepository.getById(film.getMpa().getId()));
        film.setGenres(new HashSet<>(genreRepository.findGenresByFilmId(film.getId())));
        return film;
    }

    @Override
    public Film update(Film film) {
        if (film.getId() == null) {
            log.debug("Film update - Film = {}, id is null", film);
            throw new ValidationException("Film update - Film id is null");
        }
        if (findOne(sqlRequests.SELECT_BY_ID_FILM, film.getId()).isEmpty()) {
            log.debug("Film update - Film = {}, not found", film);
            throw new NotFoundException("Film not found");
        }
        update(
                sqlRequests.UPDATE_FILM,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId()
        );
        return FilmMapper.mapToFilmDto(film);
    }

    public void deleteById(Long id) {
        delete(sqlRequests.DELETE_BY_ID_FILM, id);
    }

    @Override
    public void addLike(Long filmId, Long userId) {
        insertNotId(sqlRequests.ADD_LIKE, filmId, userId);
    }

    @Override
    public void deleteLike(Long filmId, Long userId) {
        deleteTwoId(sqlRequests.DELETE_LIKE, filmId, userId);
    }

    @Override
    public List<Film> getPopularLikesFilms(Integer sizeList) {
        return findAll().stream()
                .sorted(Comparator.comparing(film -> film.getLikes().size(), Comparator.reverseOrder()))
                .limit(sizeList)
                .toList();
    }

    public void validationById(Film film) {
        try {
            mpaRepository.getById(film.getMpa().getId());
            for (Genre g : film.getGenres()) {
                genreRepository.getById(g.getId());
            }
        } catch (Exception e) {
            log.debug("Film = {}, fail validation", film);
            throw new ValidationException(e.getMessage());
        }
    }
}
