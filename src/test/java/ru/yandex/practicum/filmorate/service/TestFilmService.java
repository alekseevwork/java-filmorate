package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestFilmService {

    private final FilmService filmService;

    public TestFilmService() {
        this.filmService = new FilmService();
    }

    @Test
    void testFindAllFilm() {
        Film film = Film.builder()
                .name("name")
                .description("description")
                .releaseDate(LocalDate.of(1997, 3, 25))
                .duration(100).build();
        assertTrue(filmService.findAll().isEmpty(), "Список не пустой");
        filmService.create(film);
        assertFalse(filmService.findAll().isEmpty(), "Список пустой");
    }

    @Test
    void testCreateFilm() {
    Film film = Film.builder()
            .name("name")
            .description("description")
            .releaseDate(LocalDate.of(1997, 3, 25))
            .duration(100).build();
    Film saveFilm = filmService.create(film);
    film.setId(1L);
        assertEquals(saveFilm, film, "Задачи не совпали.");
    }

    @Test
    void testUpdateFilm() {
        Film filmOld = Film.builder()
                .name("name")
                .description("description")
                .releaseDate(LocalDate.of(1997, 3, 25))
                .duration(100).build();

        Film filmNew = Film.builder()
                .id(1L)
                .name("name2")
                .description("description2")
                .releaseDate(LocalDate.of(1997, 3, 25))
                .duration(100).build();
        filmService.create(filmOld);
        Collection<Film> findFilm = filmService.findAll();

        assertTrue(findFilm.contains(filmOld), "Фильм не добавлен");
        assertFalse(findFilm.contains(filmNew), "Фильм не добавлен");

        filmService.update(filmNew);

        assertTrue(findFilm.contains(filmNew), "Фильм не добавлен");
        assertFalse(findFilm.contains(filmOld), "Фильм не добавлен");
    }

    @Test
    void testExceptionUpdateFilm() {
        Film filmOld = Film.builder()
                .name("name")
                .description("description")
                .releaseDate(LocalDate.of(1997, 3, 25))
                .duration(100).build();
        filmService.create(filmOld);
        Film filmNew = Film.builder()
                .name("name2")
                .description("description2")
                .releaseDate(LocalDate.of(1997, 3, 25))
                .duration(100).build();

        assertThrows(ValidationException.class, () -> {
            filmService.update(filmNew);
        });
    }
}
