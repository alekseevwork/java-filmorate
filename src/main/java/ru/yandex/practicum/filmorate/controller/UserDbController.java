package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.dal.UserRepository;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserDbController {

    private final UserRepository repository;

    @GetMapping
    public Collection<User> findAll() {
        log.info("GET /users: findAll");
        return repository.findAll();
    }

    @GetMapping("/{userId}")
    public Optional<User> findAById(@PathVariable Long userId) {
        log.info("GET /users: findAll");
        return repository.findUserById(userId);
    }

    @DeleteMapping("/{userId}")
    public void DeleteById(@PathVariable Long userId) {
        log.info("GET /users: findAll");
        repository.deleteUser(userId);
    }

    @PostMapping
    public UserDto create(@Valid @RequestBody User user) {
        log.info("POST /users: create: {}", user);
        return repository.create(user);
    }

    @PutMapping
    public UserDto update(@Valid @RequestBody User newUser) {
        log.info("PUT /users: update: {}", newUser);
        return repository.update(newUser);
    }

    @PutMapping("/{userId}/friends/{friendId}")
    public void addFriend(@PathVariable Long userId, @PathVariable Long friendId) {
        log.info("PUT /users/userId/friends/friendId: addFriend user {} - friend {}", userId, friendId);
        repository.addFriend(userId, friendId);
    }

    @DeleteMapping("/{userId}/friends/{friendId}")
    public void deleteFriend(@PathVariable Long userId, @PathVariable Long friendId) {
        log.info("DELETE /users/userId/friends/friendId: deleteFriend user {} - friend {}", userId, friendId);
        repository.deleteFriend(userId, friendId);
    }

    @GetMapping("/{userId}/friends")
    public List<User> getAllFriends(@PathVariable Long userId) {
        log.info("GET /users/userId/friends: getAllFriends {}", userId);
        return repository.getAllFriends(userId);
    }

    @GetMapping("/{userId}/friends/common/{otherId}")
    public List<User> getFriendsSharedUsers(@PathVariable Long userId, @PathVariable Long otherId) {
        log.info("GET /users/userId/friends/common/otherId: getAllFriends  userId - {}, otherId - {}", userId, otherId);
        return repository.getFriendsSharedUsers(userId, otherId);
    }
}
