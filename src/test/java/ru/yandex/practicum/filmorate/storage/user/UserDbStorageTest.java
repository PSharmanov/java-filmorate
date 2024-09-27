package ru.yandex.practicum.filmorate.storage.user;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.friendship.FriendshipStorage;

import java.util.Collection;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@ComponentScan("ru.yandex.practicum.filmorate")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserDbStorageTest {

    UserDbStorage userStorage;
    FriendshipStorage friendshipStorage;

    @Test
    void findAll() {
        Collection<User> allUsers = userStorage.findAll();
        assertThat(allUsers).extracting(User::getId).containsExactlyInAnyOrder(1L, 2L, 3L);
    }

    @Test
    void findById_notFound() {
        Optional<User> user = userStorage.findById(100);
        assertThat(user).isEmpty();
    }

    @Test
    void findById() {
        Optional<User> user = userStorage.findById(1L);
        assertThat(user).isPresent();
        assertThat(user.get().getEmail()).isEqualTo("user1@example.com");

    }

    @Test
    void findByEmail() {
        Optional<User> user = userStorage.findByEmail("user1@example.com");
        assertThat(user).isPresent();
        assertThat(user.get().getId()).isEqualTo(1L);

    }

    @Test
    void create() {
        User newUser = new User();
        newUser.setName("TestUser");
        newUser.setLogin("testLogin");
        newUser.setEmail("user7@yandex.com");
        Assertions.assertThat(userStorage.create(newUser))
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", 4L);
    }

    @Test
    void update() {
        User newUser = new User();
        newUser.setId(1L);
        newUser.setName("NewTestUser");
        newUser.setLogin("testLogin");
        newUser.setEmail("user7@yandex.com");

        Assertions.assertThat(userStorage.update(newUser))
                .isNotNull()
                .hasFieldOrPropertyWithValue("name", "NewTestUser");

    }

    @Test
    void addFriend() {
        userStorage.addFriend(2L,3L);
        Collection<User> userList = friendshipStorage.findAllFriends(2L);
        assertThat(userList).extracting(User::getId).contains(2L);

    }

    @Test
    void removeFriend() {
        userStorage.removeFriend(2L,1L);
        Collection<User> userList = friendshipStorage.findAllFriends(2L);
        assertThat(userList).isEmpty();
    }
}