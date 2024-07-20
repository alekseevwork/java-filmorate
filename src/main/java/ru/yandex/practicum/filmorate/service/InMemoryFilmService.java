package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
public class InMemoryFilmService implements FilmService {

    public final FilmStorage filmStorage;
    public final UserStorage userStorage;

    @Autowired
    public InMemoryFilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    @Override
    public void addLike(Long filmId, Long userId) {
        if (!filmStorage.getFilms().containsKey(filmId)) {
            log.debug("Film addLike - Film with id = {} not found", filmId);
            throw new NotFoundException("Film not found.");
        }
        Film film = filmStorage.getFilms().get(filmId);

        if (film.getUsersId().contains(userId)) {
            log.debug("Film addLike - User already voiced");
            throw new ValidationException("User already voiced.");
        }
        if (!userStorage.getUsers().containsKey(userId) || userId == null) {
            log.debug("Film addLike - User with id = {} not found", userId);
            throw new NotFoundException("User not found.");
        }
        film.getUsersId().add(userId);
        int oldLike = film.getLike();
        film.setLike(++oldLike);
    }

    @Override
    public void deleteLike(Long filmId, Long userId) {
        if (!filmStorage.getFilms().containsKey(filmId)) {
            log.debug("Film addLike - Film with id = {} not found", filmId);
            throw new NotFoundException("Film not found.");
        }
        Film film = filmStorage.getFilms().get(filmId);

        if (!userStorage.getUsers().containsKey(userId) || userId == null) {
            log.debug("Film addLike - User with id = {} not found", userId);
            throw new NotFoundException("User not found.");
        }
        int oldLike = film.getLike();
        film.setLike(--oldLike);
        film.getUsersId().remove(userId);
    }

    @Override
    public List<Film> getPopularLikesFilms(Integer sizeList) {
        if (sizeList == null) {
            log.debug("getPopularLikesFilms - Count is null");
            throw new ValidationException("Count is null");
        }
        return filmStorage.findAll().stream()
                .sorted(Comparator.comparingInt(Film::getLike).reversed())
                .limit(sizeList)
                .toList();
    }
}
