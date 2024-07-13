package ru.yandex.practicum.filmorate.storage;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.DuplicateFormatFlagsException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestUserStorage {

    private final InMemoryUserStorage userStorage;

    public TestUserStorage() {
        this.userStorage = new InMemoryUserStorage();
    }

    @Test
    void testFindAllUser() {
        User user = User.builder()
                .id(1L)
                .name("Bill")
                .login("loG")
                .email("example@mail.com")
                .birthday(LocalDate.parse("2002-12-03"))
                .build();
        assertTrue(userStorage.findAll().isEmpty(), "Список не пустой");
        userStorage.create(user);
        assertFalse(userStorage.findAll().isEmpty(), "Список пустой");
    }

    @Test
    void testCreateUser() {
        User user = User.builder()
                .name("Bill")
                .login("loG")
                .email("example@mail.com")
                .birthday(LocalDate.parse("2002-12-03"))
                .build();
        User saveUser = userStorage.create(user);
        user.setId(1L);
        assertEquals(saveUser, user, "Задачи не совпали.");
    }

    @Test
    void testUpdateUser() {
        User userOld = User.builder()
                .name("Bill")
                .login("loG")
                .email("example@mail.com")
                .birthday(LocalDate.parse("2002-12-03"))
                .build();

        User userNew = User.builder()
                .id(1L)
                .name("Jonn")
                .login("loG")
                .email("example@mail.com")
                .birthday(LocalDate.parse("2002-12-03"))
                .build();
        userStorage.create(userOld);
        Collection<User> findsUser = userStorage.findAll();

        assertTrue(findsUser.contains(userOld), "Фильм не добавлен");
        assertFalse(findsUser.contains(userNew), "Фильм не добавлен");

        userStorage.update(userNew);

        assertTrue(findsUser.contains(userNew), "Фильм не добавлен");
        assertFalse(findsUser.contains(userOld), "Фильм не добавлен");
    }

    @Test
    void testCreateTwoUserInEqualsEmail() {
        User userOld = User.builder()
                .name("Bill")
                .login("loG")
                .email("example@mail.com")
                .birthday(LocalDate.parse("2002-12-03"))
                .build();

        User userNew = User.builder()
                .name("Jonn")
                .login("loG")
                .email("example@mail.com")
                .birthday(LocalDate.parse("2002-12-03"))
                .build();

        userStorage.create(userOld);
        assertThrows(DuplicateFormatFlagsException.class, () -> {
            userStorage.create(userNew);
        });
    }

    @Test
    void testExceptionByEmptyIdUpdateUser() {
        User user = User.builder()
                .name("Bill")
                .login("loG")
                .email("example@mail.com")
                .birthday(LocalDate.parse("2002-12-03"))
                .build();
        assertThrows(ValidationException.class, () -> {
            userStorage.update(user);
        });
    }
}