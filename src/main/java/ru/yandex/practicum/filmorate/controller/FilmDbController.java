package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.dal.FilmRepository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.InMemoryFilmService;

import java.util.Collection;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/films")
public class FilmDbController {

    private final FilmRepository repository;

    @GetMapping
    public Collection<Film> findAll() {
        log.info("GET /films: findAll");
        return repository.findAll();
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        log.info("POST /films: create: {}", film);
        return repository.create(film);
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film newFilm) {
        log.info("PUT /films: update: {}", newFilm);
        return repository.update(newFilm);
    }

    @PutMapping("/{filmId}/like/{userId}")
    public void addLike(@Valid @PathVariable Long filmId, @Valid  @PathVariable Long userId) {
        log.info("PUT /films/filmId/like/userId: addLike: filmId - {}, userId - {}", filmId, userId);
        repository.addLike(filmId, userId);
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public void deleteLike(@Valid @PathVariable Long filmId, @Valid @PathVariable Long userId) {
        log.info("DELETE /films/filmId/like/userId: deleteLike: filmId - {}, userId - {}", filmId, userId);
        repository.addLike(filmId, userId);
    }

    @GetMapping("/popular")
    public List<Film> getPopularLikesFilms(@RequestParam(defaultValue = "10") Integer count) {
        log.info("GET /films: findAll");
        return repository.getPopularLikesFilms(count);
    }
}
