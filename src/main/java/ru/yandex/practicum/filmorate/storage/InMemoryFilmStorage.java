package ru.yandex.practicum.filmorate.storage;

import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class InMemoryFilmStorage implements FilmStorage {

    @Getter
    private final Map<Long, Film> films = new HashMap<>();
    @PositiveOrZero
    private Long filmId = 0L;

    @Override
    public Collection<Film> findAll() {
        return films.values();
    }

    @Override
    public Film create(Film film) {
        film.setId(++filmId);
        film.setLikes(new HashSet<>());
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film update(Film newFilm) {
        if (films.containsKey(newFilm.getId())) {
            log.trace("film contains in filmsMap");
            if (newFilm.getLikes() == null) {
                newFilm.setLikes(new HashSet<>());
            }
            Film oldFilm = createFilm(newFilm);
            films.put(oldFilm.getId(), oldFilm);
            return oldFilm;
        }
        log.debug("PUT ERROR: film Id not found");
        throw new NotFoundException("Фильм с id = " + newFilm.getId() + " не найден");
    }

    private Film createFilm(Film newFilm) {
        Film film = new Film();
        film.setId(newFilm.getId());
        film.setName(newFilm.getName());
        film.setDescription(newFilm.getDescription());
        film.setReleaseDate(newFilm.getReleaseDate());
        film.setDuration(newFilm.getDuration());
        return film;
    }
}
