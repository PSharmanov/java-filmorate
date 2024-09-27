package ru.yandex.practicum.filmorate.storage.friendship;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.BaseDbStorage;

import java.util.Collection;

@Repository
public class FriendshipDbStorage extends BaseDbStorage<User> implements FriendshipStorage {


    public FriendshipDbStorage(JdbcTemplate jdbcTemplate, RowMapper<User> mapper) {
        super(jdbcTemplate, mapper, User.class);
    }

    @Override
    public void addFriend(Long userId, Long friendId) {
        String sqlQuery = "INSERT INTO friendship (user_id, friend_id) VALUES (?, ?)";
        update(sqlQuery, userId, friendId);
    }

    @Override
    public void removeFriend(Long userId, Long friendId) {
        String sqlQuery = "DELETE FROM friendship WHERE user_id = ? AND friend_id = ?";
        update(sqlQuery, userId, friendId);
    }

    @Override
    public Collection<User> findAllFriends(Long id) {
        String sqlQuery = "SELECT u.*, GROUP_CONCAT(f.friend_id) AS friends " +
                "FROM users u " +
                "LEFT JOIN friendship f ON u.user_id = f.user_id AND f.status_id = 'confirmed' " +
                "WHERE u.user_id = ? " +
                "HAVING friends IS NOT NULL";
        return findMany(sqlQuery, id);
    }

    @Override
    public Collection<User> findCommonFriends(Long id, Long otherId) {
        String sqlQuery = "SELECT u.user_id, u.email, u.login, u.name, u.birthday " +
                "FROM friendship AS f " +
                "INNER JOIN friendship fr on fr.friend_id = f.friend_id " +
                "INNER JOIN users u on u.user_id = fr.friend_id " +
                "WHERE f.user_id = ? and fr.user_id = ? " +
                "AND f.friend_id <> fr.user_id AND fr.friend_id <> f.user_id";
        return findMany(sqlQuery, id, otherId);
    }
}
