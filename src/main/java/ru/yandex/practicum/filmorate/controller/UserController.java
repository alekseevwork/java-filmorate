package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final Map<Long, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> findAll() {
        log.info("GET findAll Users");
        return users.values();
    }

    @PostMapping
    public User create(@RequestBody User user) {
        log.info("Use POST: User create");
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            log.warn("POST ERROR: create User - Email is empty");
            throw new ValidationException("Электронная почта не может быть пустой");
        }
        log.info(user.getBirthday().toString());
        if (!user.getEmail().contains("@")) {
            log.warn("POST ERROR: create User - Email not validation");
            throw new ValidationException("Электронная почта должна содержать символ @");
        }
        if (user.getLogin() == null || user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            log.warn("POST ERROR: create User - Login is empty");
            throw new ValidationException("Логин не может быть пустым и содержать пробелы");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            log.debug("create User - Name is empty, replaced in Login");
            user.setName(user.getLogin());
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("POST ERROR: create User - Birthday after now date");
            throw new ValidationException("Дата рождения не может быть в будущем.");
        }
        user.setId(getNextId());
        log.trace("user set Id");
        users.put(user.getId(), user);
        log.trace("user add in usersMap");
        return user;
    }

    @PutMapping
    public User update(@RequestBody User newUser) {
        log.info("Use PUT: User update");
        if (newUser.getId() == null) {
            log.warn("PUT ERROR: update User - id is empty");
            throw new ValidationException("Id должен быть указан");
        }
        if (isEmailIn(newUser.getEmail())) {
            log.debug("c update User - Email is already");
            throw new DuplicateFormatFlagsException("Этот имейл уже используется");
        }
        if (users.containsKey(newUser.getId())) {
            log.trace("user contains in usersMap");
            User oldUser = users.get(newUser.getId());
            if (newUser.getEmail() != null) {
                log.trace("User set new email");
                oldUser.setEmail(newUser.getEmail());
            }
            if (newUser.getLogin() != null) {
                log.trace("User set new login");
                oldUser.setLogin(newUser.getLogin());
            }
            if (newUser.getName() != null) {
                log.trace("User set new name");
                oldUser.setName(newUser.getName());
            }
            if (newUser.getBirthday() != null) {
                log.trace("User set new birthday");
                oldUser.setBirthday(newUser.getBirthday());
            }
            oldUser.setEmail(newUser.getEmail());
            return oldUser;
        }
        log.warn("PUT ERROR: User id not found");
        throw new ValidationException("Пользователь с id = " + newUser.getId() + " не найден");
    }

    private boolean isEmailIn(String email) {
        log.info("Use isEmailIn");
        return users.values().stream().
                filter(user -> Objects.equals(user.getEmail(), email)).
                isParallel();
    }

    private long getNextId() {
        log.info("Use getNextId for User");
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
