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
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserRepository repository;

    @GetMapping
    public Collection<UserDto> findAll() {
        log.info("GET /users: findAll");
        return repository.findAll().stream().map(UserMapper::mapToUserDto).toList();
    }

    @GetMapping("/{userId}")
    public UserDto findAById(@PathVariable Long userId) {
        log.info("GET /users: findAById");
        return UserMapper.mapToUserDto(repository.findUserById(userId)
                .orElseThrow(() -> new NotFoundException("User by id: " + userId + " not found")));
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
    public List<UserDto> getAllFriends(@PathVariable Long userId) {
        log.info("GET /users/userId/friends: getAllFriends {}", userId);
        return repository.getAllFriends(userId).stream().map(UserMapper::mapToUserDto).toList();
    }

    @GetMapping("/{userId}/friends/common/{otherId}")
    public List<UserDto> getFriendsSharedUsers(@PathVariable Long userId, @PathVariable Long otherId) {
        log.info("GET /users/userId/friends/common/otherId: getAllFriends  userId - {}, otherId - {}", userId, otherId);
        return repository.getFriendsSharedUsers(userId, otherId).stream().map(UserMapper::mapToUserDto).toList();
    }
}
