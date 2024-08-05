package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dal.UserRepository;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class UserDbStorage implements UserStorage, UserService {

    public final UserRepository repository;

    @Autowired
    public UserDbStorage(UserRepository userRepository) {
        this.repository = userRepository;
    }

    @Override
    public User create(User user) {
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
        return repository.saveUser(user);
    }

    @Override
    public Collection<User> findAll() {
        return repository.findAllUser();
    }

    @Override
    public Optional<User> findUserById(Long userId) {
        return repository.findUserById(userId);
    }

    @Override
    public User update(User user) {
        return repository.updateUser(user);
    }

    public void deleteUserById(Long userId) {
        repository.deleteUser(userId);
    }

    @Override
    public void addFriend(Long userId, Long friendId) {
        repository.addFriend(userId, friendId);
    }

    @Override
    public void deleteFriend(Long userId, Long friendId) {
        repository.deleteFriend(userId, friendId);
    }

    @Override
    public List<User> getAllFriends(Long userId) {
        return repository.getAllFriends(userId);
    }

    @Override
    public List<User> getFriendsSharedUsers(Long userId, Long otherId) {
        return repository.getFriendsSharedUsers(userId, otherId).stream().toList();
    }
}
