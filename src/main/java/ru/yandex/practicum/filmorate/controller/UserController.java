package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.DuplicateFormatFlagsException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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
    public User create(@Valid @RequestBody User user) {
        log.info("Use POST: User create");

        if (user.getName() == null || user.getName().isBlank()) {
            log.debug("create User - Name is empty, replaced in Login");
            user.setName(user.getLogin());
        }
        user.setId(getNextId());
        log.trace("user set Id");

        users.put(user.getId(), user);
        log.trace("user add in usersMap");

        return user;
    }

    @PutMapping
    public User update(@Valid @RequestBody User newUser) {
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
            User oldUser = createUser(newUser);
            users.put(oldUser.getId(), oldUser);

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

    public User createUser(User newUser) {
        return User.builder()
                .id(newUser.getId())
                .email(newUser.getEmail())
                .login(newUser.getLogin())
                .name(newUser.getName())
                .birthday(newUser.getBirthday())
                .build();
    }
}
