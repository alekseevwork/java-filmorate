package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.dal.GenreRepository;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/genre")
public class GenreController {
    private final GenreRepository genreRepository;


    public Collection<Genre> getAll() {
        log.info("GET /genre: getAll");
        return genreRepository.getAll();
    }

    public Genre getById(Integer id) {
        log.info("GET /genre: getById");
        return genreRepository.getById(id);
    }
}
