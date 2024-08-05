package ru.yandex.practicum.filmorate.dal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
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
    private static final String INSERT_QUERY = "INSERT INTO users (email, login, name, birthday)" +
            "VALUES (?, ?, ?, ?)";
    private static final String FIND_ALL_QUERY = "SELECT * FROM users";
    private static final String UPDATE_QUERY = "UPDATE users SET email = ?, login = ?, name = ?, birthday = ? WHERE id = ?";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM users WHERE id = ?";
    private static final String DELETE_BY_ID_QUERY = "DELETE FROM users WHERE id = ?";

    private static final String ADD_FRIEND_QUERY = "INSERT INTO friend(user_id, friend_id, status)" +
            "VALUES (?, ?, ?)";
    private static final String DELETE_FRIEND_QUERY = "DELETE FROM friend WHERE user_id = ? AND friend_id = ?";
    private static final String FIND_STATUS_FRIENDS = "SELECT status FROM friend WHERE user_id = ? AND friend_id = ?";
    private static final String UPDATE_FRIEND_QUERY = "UPDATE friend SET status = ? WHERE user_id = ? AND friend_id = ?";
    private static final String FIND_ALL_FRIEND_QUERY = "SELECT friend_id FROM friend WHERE user_id = ?";

    public UserRepository(JdbcTemplate jdbc, RowMapper<User> mapper) {
        super(jdbc, mapper, User.class);
    }

    public List<User> findAllUser() {
        return findMany(FIND_ALL_QUERY);
    }

    public Optional<User> findUserById(Long userId) {
        return findOne(FIND_BY_ID_QUERY, userId);
    }

    public User saveUser(User user) {
        Long id = insert(
                INSERT_QUERY,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday()
        );
        user.setId(id);
        return user;
    }

    public User updateUser(User user) {
        if (findOne(FIND_BY_ID_QUERY, user.getId()).isEmpty()) {
            log.debug("User create - User = {}, not found", user);
            throw new NotFoundException("User not found");
        }
        update(
                UPDATE_QUERY,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId()
        );
        return user;
    }

    public void deleteUser(Long userId) {
        delete(DELETE_BY_ID_QUERY, userId);
    }

    public void addFriend(Long userId, Long friendId) {
        isExist(userId, friendId);

        boolean status = getBoolean(FIND_STATUS_FRIENDS, friendId, userId);

        if (status) {
            insertNotId(ADD_FRIEND_QUERY, userId, friendId, true);
            updateFriendStatus(true, friendId, userId);
        } else {
            insertNotId(ADD_FRIEND_QUERY, userId, friendId, false);
        }
    }

    public void deleteFriend(Long userId, Long friendId) {
        isExist(userId, friendId);

        if (getAllFriendsId(userId).contains(friendId)) {
            deleteTwoId(DELETE_FRIEND_QUERY, userId, friendId);
        }
    }

    public void updateFriendStatus(boolean status, Long userId, Long friendId) {
        update(UPDATE_FRIEND_QUERY, status, userId, friendId);
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
            users.add(findOne(FIND_BY_ID_QUERY, id).get());
        }
        return users;
    }

    public List<Long> getAllFriendsId(Long id) {
        List<Long> friends = new ArrayList<>();
        jdbc.query(FIND_ALL_FRIEND_QUERY, rs -> {
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
