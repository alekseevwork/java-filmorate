package ru.yandex.practicum.filmorate.storage;

import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.DuplicatedMailException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.DuplicateFormatFlagsException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class InMemoryUserStorage implements UserStorage {

    @Getter
    private final Map<Long, User> users = new HashMap<>();
    @PositiveOrZero
    private Long userId = 0L;

    @Override
    public Collection<User> findAll() {
        return users.values();
    }

    @Override
    public Optional<User> findUserById(Long userId) {
        if (users.containsKey(userId)) {
            return Optional.of(users.get(userId));
        }
        return Optional.empty();
    }

    @Override
    public User create(User user) {
        if (isEmailIn(user.getEmail(), 0)) {
            log.debug("POST ERROR: User create  - Email = {}, is already", user.getEmail());
            throw new DuplicateFormatFlagsException("Этот имейл уже используется");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            log.debug("User create - Name is empty, replaced in Login");
            user.setName(user.getLogin());
        }
        user.setId(++userId);
        log.trace("user set Id");

        users.put(user.getId(), user);
        log.trace("user add in usersMap");
        return user;
    }

    @Override
    public User update(User newUser) {
        if (newUser.getId() == null) {
            log.warn("PUT ERROR: update User - id is empty");
            throw new ValidationException("Id должен быть указан");
        }
        if (isEmailIn(newUser.getEmail(), 1)) {
            log.debug("PUT ERROR: User update  - Email = {}, is already", newUser.getEmail());
            throw new DuplicatedMailException("Этот имейл уже используется");
        }
        if (users.containsKey(newUser.getId())) {
            User oldUser = createUser(newUser);
            users.put(oldUser.getId(), oldUser);
            return oldUser;
        }
        log.warn("PUT ERROR: User id not found");
        throw new NotFoundException("Пользователь с id = " + newUser.getId() + " не найден");
    }

    private boolean isEmailIn(String email, int pointer) {
        log.info("Use isEmailIn");
        return users.values().stream().filter(user -> user.getEmail().equals(email)).toList().size() > pointer;
    }

    private User createUser(User newUser) {
        User user = new User();
        user.setId(newUser.getId());
        user.setEmail(newUser.getEmail());
        user.setLogin(newUser.getLogin());
        user.setName(newUser.getName());
        user.setBirthday(newUser.getBirthday());
        return user;
    }
}
