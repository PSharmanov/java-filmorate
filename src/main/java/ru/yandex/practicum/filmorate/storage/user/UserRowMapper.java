package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

@Component
public class UserRowMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        User user = new User();
        user.setId(rs.getLong("user_id"));
        user.setName(rs.getString("name"));
        user.setLogin(rs.getString("login"));
        user.setEmail(rs.getString("email"));

        Timestamp registrationDate = rs.getTimestamp("registrationDate");
        user.setRegistrationDate(registrationDate.toInstant());

        Date birthday = rs.getDate("birthday");
        user.setBirthday(birthday.toLocalDate());

        return user;
    }
}
