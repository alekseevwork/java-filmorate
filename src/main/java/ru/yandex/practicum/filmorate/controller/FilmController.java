package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final Map<Long, Film> films = new HashMap<>();

    @GetMapping
    public Collection<Film> findAll() {
        log.info("GET findAll Films");
        return films.values();
    }

    @PostMapping
    public Film create(@RequestBody Film film) {
        log.info("Use POST: Film create");
        if (film.getName() == null || film.getName().isBlank()) {
            log.debug("POST ERROR: create Film - Name is empty");
            throw new ValidationException("Название не может быть пустым");
        }
        if (film.getDescription().length() > 200) {
            log.debug("POST ERROR: create Film - Description > 200 symbols");
            throw new ValidationException("Максимальная длина описания — 200 символов");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.debug("POST ERROR: create Film - ReleaseDate is before 28-12-1895");
            throw new ValidationException("Дата релиза — не раньше 28 декабря 1895 года");
        }
        if (film.getDuration() < 0) {
            log.debug("POST ERROR: create Film - Duration < 0");
            throw new ValidationException("Продолжительность фильма должна быть положительным числом");
        }
        film.setId(getNextId());
        log.trace("film set Id");
        films.put(film.getId(), film);
        log.trace("film add in filmsMap");
        return film;
    }

    @PutMapping
    public Film update(@RequestBody Film newFilm) {
        log.info("Use PUT Film update");
        if (newFilm.getId() == null) {
            log.debug("PUT ERROR: Id is null");
            throw new ValidationException("Id должен быть указан");
        }
        if (films.containsKey(newFilm.getId())) {
            log.trace("film contains in filmsMap");
            Film oldFilm = films.get(newFilm.getId());
            if (newFilm.getName() != null && !newFilm.getName().isBlank()) {
                log.trace("film set new name");
                oldFilm.setName(newFilm.getName());
            }
            if (newFilm.getDescription() != null && newFilm.getDescription().length() < 200) {
                log.trace("film set new description");
                oldFilm.setDescription(newFilm.getDescription());
            }
            if (newFilm.getReleaseDate() != null &&
                    newFilm.getReleaseDate().isAfter(LocalDate.of(1895, 12, 28))) {
                log.trace("film set new release date");
                oldFilm.setReleaseDate(newFilm.getReleaseDate());
            }
            if (newFilm.getDuration() != null && newFilm.getDuration() > 0) {
                log.trace("film set new duration");
                oldFilm.setDuration(newFilm.getDuration());
            }
            return oldFilm;
        }
        log.debug("PUT ERROR: Id not found");
        throw new ValidationException("Фильм с id = " + newFilm.getId() + " не найден");
    }


    private long getNextId() {
        log.info("Use getNextId for Film");
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
