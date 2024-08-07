package ru.yandex.practicum.filmorate.dal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Repository
public class UserRepository extends BaseRepository<User>{
    private static final String INSERT_USER = "INSERT INTO users (email, login, name, birthday)" +
            "VALUES (?, ?, ?, ?)";
    private static final String SELECT_ALL_USER = "SELECT * FROM users";
    private static final String UPDATE_USER = "UPDATE users SET email = ?, login = ?, name = ?, birthday = ? WHERE id = ?";
    private static final String SELECT_BY_ID_USER = "SELECT * FROM users WHERE id = ?";
    private static final String DELETE_BY_ID_USER = "DELETE FROM users WHERE id = ?";

    private static final String INSERT_FRIEND = "INSERT INTO friend(user_id, friend_id, status)" +
            "VALUES (?, ?, ?)";
    private static final String DELETE_FRIEND = "DELETE FROM friend WHERE user_id = ? AND friend_id = ?";
    private static final String SELECT_STATUS_FRIENDS = "SELECT status FROM friend WHERE user_id = ? AND friend_id = ?";
    private static final String UPDATE_FRIEND = "UPDATE friend SET status = ? WHERE user_id = ? AND friend_id = ?";
    private static final String SELECT_ALL_FRIEND = "SELECT friend_id FROM friend WHERE user_id = ?";

    public UserRepository(JdbcTemplate jdbc, RowMapper<User> mapper) {
        super(jdbc, mapper, User.class);
    }

    public List<User> findAllUser() {
        return findMany(SELECT_ALL_USER);
    }

    public Optional<User> findUserById(Long userId) {
        return findOne(SELECT_BY_ID_USER, userId);
    }

    public UserDto createUser(User user) {
        Long id = insert(
                INSERT_USER,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday()
        );
        user.setId(id);
        return UserMapper.mapToUserDto(user);
    }

    public User updateUser(User user) {
        if (findOne(SELECT_BY_ID_USER, user.getId()).isEmpty()) {
            log.debug("User update - User = {}, not found", user);
            throw new NotFoundException("User not found");
        }
        update(
                UPDATE_USER,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId()
        );
        return user;
    }

    public void deleteUser(Long userId) {
        delete(DELETE_BY_ID_USER, userId);
    }

    public void addFriend(Long userId, Long friendId) {
        isExist(userId, friendId);

        boolean status = getBoolean(SELECT_STATUS_FRIENDS, friendId, userId);

        if (status) {
            insertNotId(INSERT_FRIEND, userId, friendId, true);
            updateFriendStatus(true, friendId, userId);
        } else {
            insertNotId(INSERT_FRIEND, userId, friendId, false);
        }
    }

    public void deleteFriend(Long userId, Long friendId) {
        isExist(userId, friendId);

        if (getAllFriendsId(userId).contains(friendId)) {
            deleteTwoId(DELETE_FRIEND, userId, friendId);
        }
    }

    public void updateFriendStatus(boolean status, Long userId, Long friendId) {
        update(UPDATE_FRIEND, status, userId, friendId);
        log.info("User with Id: {} update status friend: {}", userId, friendId);

    }

    public List<User> getAllFriends(Long userId) {
        if (findUserById(userId).isEmpty()) {
            log.debug("User getAllFriends - User with id = {} not found", userId);
            throw new NotFoundException("User not found");
        }
        List<Long> friends = getAllFriendsId(userId);
        List<User> users = new ArrayList<>();
        if (friends.isEmpty()) {
            log.info("User with Id: {} not found friends", userId);
            return users;
        }

        for (Long id: friends) {
            users.add(findOne(SELECT_BY_ID_USER, id).get());
        }
        return users;
    }

    public List<Long> getAllFriendsId(Long id) {
        List<Long> friends = new ArrayList<>();
        jdbc.query(SELECT_ALL_FRIEND, rs -> {
            while (rs.next()) {
                friends.add(rs.getLong("friend_id"));
            }
            return friends;
        }, id);

        return friends;
    }

    public Set<User> getFriendsSharedUsers(Long userId, Long otherId) {
        Set<User> sharedFriends = new HashSet<>();

        sharedFriends.addAll(getAllFriends(userId));
        sharedFriends.addAll(getAllFriends(otherId));

        return sharedFriends.stream()
                .filter(user -> !Objects.equals(otherId, user.getId()))
                .filter(user -> !Objects.equals(userId, user.getId()))
                .collect(Collectors.toSet());
    }

    public void isExist(Long userId, Long friendId) {
        if (findUserById(userId).isEmpty()) {
            log.debug("User isExist - User with id = {} not found", userId);
            throw new NotFoundException("User by " + userId + " not found");
        }
        if (findUserById(friendId).isEmpty()) {
            log.debug("User isExist - User with id = {} not found", friendId);
            throw new NotFoundException("User by " + friendId + " not found");
        }
    }
}
