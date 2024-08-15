package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class InMemoryUserService implements UserService {

    @Qualifier("UserDbStorage")
    public final InMemoryUserStorage userStorage;

    @Autowired
    public InMemoryUserService(InMemoryUserStorage userStorage) {
        this.userStorage = userStorage;
    }

    @Override
    public void addFriend(Long userId, Long friendId) {
        if (!userStorage.getUsers().containsKey(userId)) {
            log.debug("User addFriend - User with id = {} not found", userId);
            throw new NotFoundException("User not found.");
        }

        if (!userStorage.getUsers().containsKey(friendId)) {
            log.debug("User addFriend - Friend with id = {} not found", friendId);
            throw new NotFoundException("Friend not found.");
        }
    }

    @Override
    public void deleteFriend(Long userId, Long friendId) {

        if (!userStorage.getUsers().containsKey(userId)) {
            log.debug("User deleteFriend - User with id = {} not found", userId);
            throw new NotFoundException("User not found.");
        }

        if (!userStorage.getUsers().containsKey(friendId)) {
            log.debug("User deleteFriend - Friend with id = {} not found", friendId);
            throw new NotFoundException("Friend not found.");
        }
    }

    @Override
    public List<User> getAllFriends(Long userId) {
        List<User> friendsList = new ArrayList<>();

        if (!userStorage.getUsers().containsKey(userId)) {
            log.debug("User getAllFriends - User with id = {} not found", userId);
            throw new NotFoundException("User not found.");
        }

        return friendsList;
    }

    @Override
    public List<User> getFriendsSharedUsers(Long userId, Long otherId) {
        List<User> friendsList = new ArrayList<>();

        if (!userStorage.getUsers().containsKey(userId)) {
            log.debug("User getFriendsSharedUsers - User with id = {} not found", userId);
            throw new NotFoundException("User not found.");
        }

        if (!userStorage.getUsers().containsKey(otherId)) {
            log.debug("User getFriendsSharedUsers - other User with id = {} not found", otherId);
            throw new NotFoundException("Friend not found.");
        }

        return friendsList;
    }
}
