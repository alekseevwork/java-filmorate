package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {

    private final Map<Long, Film> films = new HashMap<>();
    private Long filmId = 0L;

    public Collection<Film> findAll() {
        return films.values();
    }

    public Film create(Film film) {
        film.setId(++filmId);
        films.put(film.getId(), film);
        return film;
    }

    public Film update(Film newFilm) {
        if (films.containsKey(newFilm.getId())) {
            log.trace("film contains in filmsMap");
            Film oldFilm = createFilm(newFilm);
            films.put(oldFilm.getId(), oldFilm);
            return oldFilm;
        }
        log.debug("PUT ERROR: Id not found");
        throw new ValidationException("Фильм с id = " + newFilm.getId() + " не найден");
    }

    private Film createFilm(Film newFilm) {
        return Film.builder()
                .id(newFilm.getId())
                .name(newFilm.getName())
                .description(newFilm.getDescription())
                .releaseDate(newFilm.getReleaseDate())
                .duration(newFilm.getDuration())
                .build();
    }
}
