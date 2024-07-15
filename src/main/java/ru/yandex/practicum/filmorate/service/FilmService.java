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
public class FilmService {

    public final FilmStorage filmStorage;
    public final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public void addLike(Long filmId, Long userId) {
        if (!filmStorage.getFilms().containsKey(filmId)) {
            log.debug("Film addLike - Film not found");
            throw new NotFoundException("Film not found.");
        }
        Film film = filmStorage.getFilms().get(filmId);

        if (film.getUsersId().contains(userId)) {
            log.debug("Film addLike - User already voiced");
            throw new ValidationException("User already voiced.");
        }
        if (!userStorage.getUsers().containsKey(userId) || userId == null) {
            throw new NotFoundException("User not found.");
        }
        film.getUsersId().add(userId);
        int oldLike = film.getLike();
        film.setLike(++oldLike);
        System.out.println(film);

    }

    public void deleteLike(Long filmId, Long userId) {
        if (!filmStorage.getFilms().containsKey(filmId)) {
            log.debug("Film addLike - Film not found");
            throw new NotFoundException("Film not found.");
        }
        Film film = filmStorage.getFilms().get(filmId);

        if (!userStorage.getUsers().containsKey(userId) || userId == null) {
            throw new NotFoundException("User not found.");
        }
        int oldLike = film.getLike();
        film.setLike(--oldLike);
        film.getUsersId().remove(userId);
    }

    public List<Film> getPopularLikesFilms(Integer sizeList) {
        if (sizeList == null) {
            throw new ValidationException("Count is null");
        }
        return filmStorage.findAll().stream()
                .sorted(Comparator.comparingInt(Film::getLike).reversed())
                .limit(sizeList)
                .toList();
    }
}
