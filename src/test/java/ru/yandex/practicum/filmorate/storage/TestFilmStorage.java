package ru.yandex.practicum.filmorate.storage;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestFilmStorage {

    private final InMemoryFilmStorage filmStorage;

    public TestFilmStorage() {
        this.filmStorage = new InMemoryFilmStorage();
    }

    @Test
    void testFindAllFilm() {
        Film film = new Film();
        film.setName("name");
        film.setDescription("description");
        film.setReleaseDate(LocalDate.of(1997, 3, 25));
        film.setDuration(100);

        assertTrue(filmStorage.findAll().isEmpty(), "Список не пустой");
        filmStorage.create(film);
        assertFalse(filmStorage.findAll().isEmpty(), "Список пустой");
    }

    @Test
    void testCreateFilm() {
        Film film = new Film();
        film.setName("name");
        film.setDescription("description");
        film.setReleaseDate(LocalDate.of(1997, 3, 25));
        film.setDuration(100);

        Film saveFilm = filmStorage.create(film);
        film.setId(1L);
        assertEquals(saveFilm, film, "Задачи не совпали.");
    }

    @Test
    void testUpdateFilm() {
        Film filmOld = new Film();
        filmOld.setName("name");
        filmOld.setDescription("description");
        filmOld.setReleaseDate(LocalDate.of(1997, 3, 25));
        filmOld.setDuration(100);

        Film filmNew = new Film();
        filmNew.setId(1L);
        filmNew.setName("name2");
        filmNew.setDescription("description2");
        filmNew.setReleaseDate(LocalDate.of(1997, 3, 25));
        filmNew.setDuration(100);

        filmStorage.create(filmOld);
        Collection<Film> findFilm = filmStorage.findAll();

        assertTrue(findFilm.contains(filmOld), "Фильм не добавлен");
        assertFalse(findFilm.contains(filmNew), "Фильм не добавлен");

        filmStorage.update(filmNew);

        assertTrue(findFilm.contains(filmNew), "Фильм не добавлен");
        assertFalse(findFilm.contains(filmOld), "Фильм не добавлен");
    }

    @Test
    void testExceptionUpdateFilm() {
        Film filmOld = new Film();
        filmOld.setName("name");
        filmOld.setDescription("description");
        filmOld.setReleaseDate(LocalDate.of(1997, 3, 25));
        filmOld.setDuration(100);

        Film filmNew = new Film();
        filmNew.setId(1L);
        filmNew.setName("name2");
        filmNew.setDescription("description2");
        filmNew.setReleaseDate(LocalDate.of(1997, 3, 25));
        filmNew.setDuration(100);

        assertThrows(NotFoundException.class, () -> {
            filmStorage.update(filmNew);
        });
    }
}
