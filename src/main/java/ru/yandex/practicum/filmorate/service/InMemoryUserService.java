package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class InMemoryUserService implements UserService {

    public final UserStorage userStorage;

    @Autowired
    public InMemoryUserService(UserStorage userStorage) {
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

        userStorage.getUsers().get(userId).getFriends().add(friendId);
        userStorage.getUsers().get(friendId).getFriends().add(userId);
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
        userStorage.getUsers().get(userId).getFriends().remove(friendId);
        userStorage.getUsers().get(friendId).getFriends().remove(userId);
    }

    @Override
    public List<User> getAllFriends(Long userId) {
        List<User> friendsList = new ArrayList<>();

        if (!userStorage.getUsers().containsKey(userId)) {
            log.debug("User getAllFriends - User with id = {} not found", userId);
            throw new NotFoundException("User not found.");
        }

        User user = userStorage.getUsers().get(userId);

        for (Long friendId : user.getFriends()) {
            friendsList.add(userStorage.getUsers().get(friendId));
        }
        return friendsList;
    }

    @Override
    public List<User> getFriendsSharedUsers(Long userId, Long otherId) {
        List<User> friendsList = new ArrayList<>();
        Set<Long> friendsIdSet = new HashSet<>();

        if (!userStorage.getUsers().containsKey(userId)) {
            log.debug("User getFriendsSharedUsers - User with id = {} not found", userId);
            throw new NotFoundException("User not found.");
        }

        if (!userStorage.getUsers().containsKey(otherId)) {
            log.debug("User getFriendsSharedUsers - other User with id = {} not found", otherId);
            throw new NotFoundException("Friend not found.");
        }
        User user1 = userStorage.getUsers().get(userId);
        User user2 = userStorage.getUsers().get(otherId);

        friendsIdSet.addAll(user1.getFriends());
        friendsIdSet.addAll(user2.getFriends());

        friendsIdSet.remove(user1.getId());
        friendsIdSet.remove(user2.getId());

        for (Long id : friendsIdSet) {
            friendsList.add(userStorage.getUsers().get(id));
        }
        return friendsList;
    }
}
