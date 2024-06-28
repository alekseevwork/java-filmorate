package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

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
    public Film create(@Valid @RequestBody Film film) {
        log.info("Use POST: Film create");

        film.setId(getNextId());
        films.put(film.getId(), film);

        return film;
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film newFilm) {
        log.info("Use PUT Film update");

        if (films.containsKey(newFilm.getId())) {
            log.trace("film contains in filmsMap");
            Film oldFilm = createFilm(newFilm);
            films.put(oldFilm.getId(), oldFilm);
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

    public Film createFilm(Film newFilm) {
        return Film.builder()
                .id(newFilm.getId())
                .name(newFilm.getName())
                .description(newFilm.getDescription())
                .releaseDate(newFilm.getReleaseDate())
                .duration(newFilm.getDuration())
                .build();
    }
}
