package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.dal.MpaRepository;
import ru.yandex.practicum.filmorate.dto.MpaDto;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/mpa")
public class MpaController {

    private final MpaRepository mpaRepository;

    @GetMapping
    public List<MpaDto> getAll() {
        log.info("GET /mpa: findAll");
        return mpaRepository.getAll();
    }

    @GetMapping("/{id}")
    public Mpa getById(@PathVariable Long id) {
        log.info("GET /mpa: getById");
        return mpaRepository.getById(id);
    }
}
