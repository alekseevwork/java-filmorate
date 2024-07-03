package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmInterface {

    Film create(Film film);

    Collection<Film> findAll();

    Film update(Film film);
}
