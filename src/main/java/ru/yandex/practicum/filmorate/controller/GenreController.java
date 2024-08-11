package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.dal.GenreRepository;
import ru.yandex.practicum.filmorate.dto.GenreDto;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/genres")
public class GenreController {
    private final GenreRepository genreRepository;

    @GetMapping
    public List<GenreDto> getAll() {
        log.info("GET /genre: getAll");
        return genreRepository.getAll();
    }

    @GetMapping("/{id}")
    public GenreDto getById(@PathVariable Long id) {
        log.info("GET /genre: getById");
        return genreRepository.getById(id);
    }
}
