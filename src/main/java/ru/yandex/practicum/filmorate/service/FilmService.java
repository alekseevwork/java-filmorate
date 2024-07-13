package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.PutFilmsLikeException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
public class FilmService {

    public final FilmStorage filmStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public void addLike(Long filmId, Long userId) {
        if (!filmStorage.getFilms().containsKey(filmId)) {
            log.debug("Film addLike - Film not found");
            throw new PutFilmsLikeException("Film not found.");
        }
        Film film = filmStorage.getFilms().get(filmId);

        if (film.getUsersId().contains(userId)) {
            log.debug("Film addLike - User already voiced");
            throw new ValidationException("User already voiced.");
        }

        film.getUsersId().add(userId);
        int nowLikes = film.getLike();
        film.setLike(++nowLikes);
    }

    public void deleteLike(Long filmId, Long userId) {
        if (!filmStorage.getFilms().containsKey(filmId)) {
            log.debug("Film addLike - Film not found");
            throw new PutFilmsLikeException("Film not found.");
        }
        Film film = filmStorage.getFilms().get(filmId);

        film.getUsersId().remove(userId);
        int nowLikes = film.getLike();
        film.setLike(--nowLikes);
    }

    public List<Film> getPopularLikesFilms(int sizeList) {
        return filmStorage.findAll().stream()
                .sorted(Comparator.comparingInt(Film::getLike))
                .limit(sizeList).toList();
    }
}
