package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.dal.MpaRepository;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Collection;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/mpa")
public class MpaController {

    private final MpaRepository mpaRepository;

    public Collection<Mpa> getAll() {
        log.info("GET /mpa: findAll");
        return mpaRepository.getAll();
    }

    public Mpa getById(Integer id) {
        log.info("GET /mpa: getById");
        return mpaRepository.getById(id);
    }
}
