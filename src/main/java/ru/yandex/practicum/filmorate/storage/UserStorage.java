package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Map;

public interface UserStorage {

    User create(User user);

    Collection<User> findAll();

    User update(User user);

    Map<Long, User> getUsers();
}
