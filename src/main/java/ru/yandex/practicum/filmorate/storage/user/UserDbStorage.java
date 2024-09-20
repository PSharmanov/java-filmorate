package ru.yandex.practicum.filmorate.storage.user;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.BaseDbStorage;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
@FieldDefaults(makeFinal = true, level = AccessLevel.PROTECTED)
public class UserDbStorage extends BaseDbStorage<User> implements UserStorage {

    public UserDbStorage(JdbcTemplate jdbcTemplate, RowMapper<User> mapper) {
        super(jdbcTemplate, mapper, User.class);
    }

    @Override
    public Collection<User> findAll() {
        String sqlQuery = "SELECT * FROM users";
        return findMany(sqlQuery);
    }

    @Override
    public Optional<User> findById(long userId) {
        String sqlQuery = "SELECT * FROM users WHERE user_id = ?";
        Optional<User> user = findOne(sqlQuery, userId);

        if (user.isPresent()) {
            String sqlQueryFriends = "SELECT friend_id FROM friendship WHERE user_id = ?";
            List<Long> friends = jdbcTemplate.queryForList(sqlQueryFriends, Long.class, userId);
            user.get().setFriends(new HashSet<>(friends));
        }

        return user;
    }

    public Optional<User> findByEmail(String email) {
        String sqlQuery = "SELECT * FROM users WHERE email = ?";
        return findOne(sqlQuery, email);
    }

    @Override
    public User create(User user) {
        String sqlQuery = "INSERT INTO users (name, login, email, birthday, registrationDate) VALUES (?, ?, ?, ?, NOW())";
        long id = insert(
                sqlQuery,
                user.getName(),
                user.getEmail(),
                user.getLogin(),
                user.getBirthday()
        );
        user.setId(id);
        return user;
    }

    @Override
    public User update(User user) {
        String sqlQuery = "UPDATE users SET name = ?, email = ?, login = ?, birthday = ? WHERE user_id = ?";
        update(
                sqlQuery,
                user.getName(),
                user.getEmail(),
                user.getLogin(),
                user.getBirthday(),
                user.getId()
        );
        return user;
    }

    @Override
    public void addFriend(Long id, Long friendId) {
        String sqlQuery = "INSERT INTO friendship (user_id, friend_id, status_id) VALUES (?, ?, ?)";
        update(sqlQuery, id, friendId, "pending");
    }

    @Override
    public void removeFriend(long userId, long friendId) {
        String sqlQuery = "DELETE FROM friendship WHERE user_id = ? AND friend_id = ?";
        jdbcTemplate.update(sqlQuery, userId, friendId);
    }

}
