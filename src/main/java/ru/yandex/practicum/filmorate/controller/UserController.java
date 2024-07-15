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
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @GetMapping
    public Collection<User> findAll() {
        log.info("GET /users: findAll");
        return userService.userStorage.findAll();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        log.info("POST /users: create: {}", user);
        return userService.userStorage.create(user);
    }

    @PutMapping
    public User update(@Valid @RequestBody User newUser) {
        log.info("PUT /users: update: {}", newUser);
        return userService.userStorage.update(newUser);
    }

    @PutMapping("/{userId}/friends/{friendId}")
    public void addFriend(@PathVariable Long userId, @PathVariable Long friendId) {
        log.info("PUT /users/userId/friends/friendId: addFriend user {} - friend {}", userId, friendId);
        userService.addFriend(userId, friendId);
    }

    @DeleteMapping("/{userId}/friends/{friendId}")
    public void deleteFriend(@PathVariable Long userId, @PathVariable Long friendId) {
        log.info("DELETE /users/userId/friends/friendId: deleteFriend user {} - friend {}", userId, friendId);
        userService.deleteFriend(userId, friendId);
    }

    @GetMapping("/{userId}/friends")
    public List<User> getAllFriends(@PathVariable Long userId) {
        log.info("GET /users/userId/friends: getAllFriends {}", userId);
        return userService.getAllFriends(userId);
    }

    @GetMapping("/{userId}/friends/common/{otherId}")
    public List<User> getFriendsSharedUsers(@PathVariable Long userId, @PathVariable Long otherId) {
        log.info("GET /users/userId/friends/common/otherId: getAllFriends  userId - {}, otherId - {}", userId, otherId);
        return userService.getFriendsSharedUsers(userId, otherId);
    }
}
