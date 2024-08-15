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
        User user = new User();
        user.setId(1L);
        user.setName("Bill");
        user.setLogin("loG");
        user.setEmail("example@mail.com");
        user.setBirthday(LocalDate.parse("2002-12-03"));

        assertTrue(userStorage.findAll().isEmpty(), "Список не пустой");
        userStorage.create(user);
        assertFalse(userStorage.findAll().isEmpty(), "Список пустой");
    }

    @Test
    void testCreateUser() {
        User user = new User();
        user.setId(1L);
        user.setName("Bill");
        user.setLogin("loG");
        user.setEmail("example@mail.com");
        user.setBirthday(LocalDate.parse("2002-12-03"));

        User saveUser = userStorage.create(user);
        user.setId(1L);
        assertEquals(saveUser, user, "Задачи не совпали.");
    }

    @Test
    void testUpdateUser() {
        User userOld = new User();
        userOld.setName("Bill");
        userOld.setLogin("loG");
        userOld.setEmail("example@mail.com");
        userOld.setBirthday(LocalDate.parse("2002-12-03"));

        User userNew = new User();
        userNew.setId(1L);
        userNew.setName("Jonn");
        userNew.setLogin("loG");
        userNew.setEmail("example@mail.com");
        userNew.setBirthday(LocalDate.parse("2002-12-03"));

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
        User userOld = new User();
        userOld.setName("Bill");
        userOld.setLogin("loG");
        userOld.setEmail("example@mail.com");
        userOld.setBirthday(LocalDate.parse("2002-12-03"));

        User userNew = new User();
        userNew.setId(1L);
        userNew.setName("Jonn");
        userNew.setLogin("loG");
        userNew.setEmail("example@mail.com");
        userNew.setBirthday(LocalDate.parse("2002-12-03"));

        userStorage.create(userOld);
        assertThrows(DuplicateFormatFlagsException.class, () -> {
            userStorage.create(userNew);
        });
    }

    @Test
    void testExceptionByEmptyIdUpdateUser() {
        User user = new User();
        user.setName("Bill");
        user.setLogin("loG");
        user.setEmail("example@mail.com");
        user.setBirthday(LocalDate.parse("2002-12-03"));

        assertThrows(ValidationException.class, () -> {
            userStorage.update(user);
        });
    }
}