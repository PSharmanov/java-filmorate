package ru.yandex.practicum.filmorate.service.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.DuplicatedDataException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserStorage userStorage;

    public Collection<User> findAll() {
        log.info("Запрос на получение списка всех пользователей");
        return userStorage.findAll();
    }

    public User create(User user) {
        log.info("Запрос на создание пользователя");

        if (isUsersContainsEmail(user.getEmail())) {
            log.warn("Имейл" + user.getEmail() + " уже используется");
            throw new DuplicatedDataException("Этот имейл уже используется");
        }

        if (user.getName() == null || user.getName().isBlank()) {
            log.debug("Имя пользователя не заполнено, в место имени использован логин.");
            user.setName(user.getLogin());
        }

        user.setId(getNextId());
        user.setRegistrationDate(Instant.now());

        userStorage.create(user);

        log.info("Добавлен пользователь с id= " + user.getId());

        return user;
    }

    public User update(User newUser) {
        log.info("Обновление данных пользователя c id " + newUser.getId());

        if (newUser.getId() == null) {
            log.warn("В запросе на обновление не указан id");
            throw new ValidationException("Id должен быть указан");
        }

        if (userStorage.findById(newUser.getId()).isEmpty()) {
            log.warn("Обновление данных пользователя не произошло, не найден пользователь с id " + newUser.getId());
            throw new NotFoundException("User с id = " + newUser.getId() + " не найден");
        }

        User oldUser = userStorage.findById(newUser.getId()).get();

        if (isUsersContainsEmail(newUser.getEmail())) {
            log.warn("В запросе на обновление указан используемый email");
            throw new DuplicatedDataException("Этот имейл уже используется");
        }

        oldUser.setEmail(newUser.getEmail());
        oldUser.setLogin(newUser.getLogin());
        oldUser.setName(newUser.getName());
        oldUser.setBirthday(newUser.getBirthday());
        userStorage.update(oldUser);
        log.info("Обновлены данные пользователя с id " + oldUser.getId());

        return oldUser;
    }

    // вспомогательный метод для генерации идентификатора нового пользователя
    private Long getNextId() {
        long currentMaxId = userStorage.findAll()
                .stream()
                .map(User::getId)
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    // проверка содержит Map users указанный email или нет
    private boolean isUsersContainsEmail(String email) {
        List<String> listEmail = userStorage.findAll()
                .stream()
                .map(User::getEmail)
                .toList();
        return listEmail.contains(email);
    }

    //получение пользователя по id
    public User findById(long userId) {
        log.info("Запрос на получение пользователя с id" + userId);
        Optional<User> user = userStorage.findById(userId);
        if (user.isEmpty()) {
            log.warn("Пользователь с id" + userId + "не найден");
            throw new NotFoundException("Пользователь не найден");
        }
        return user.get();
    }

    //добавление в друзья
    public void addFriend(Long userId, Long friendId) {
        log.info("Добавление друга с id:" + friendId + " пользователю с id: " + userId);
        if (userId < 0) {
            log.warn("Пользователь добавляющий в друзья не найден. Id пользователя: " + userId);
            throw new NotFoundException("Пользователь добавляющий в друзья не найден.");
        }

        if (friendId < 0) {
            log.warn("Пользователь добавляемый в друзья не найден. Id пользователя: " + friendId);
            throw new NotFoundException("Пользователь добавляемый в друзья не найден.");
        }

        findById(userId).getFriends().add(friendId);
        findById(friendId).getFriends().add(userId);
        log.info("Добавлен друга с id:" + friendId + " пользователю с id: " + userId);
    }

    //удаление из друзей
    public void removeFriend(long userId, long friendId) {
        findById(userId).getFriends().remove(friendId);
        findById(friendId).getFriends().remove(userId);
        log.info("Удален друга с id:" + friendId + "у пользователя с id: " + userId);
    }


    //получение всех друзей пользователя
    public Collection<User> findAllFriends(long id) {
        log.info("Запрос на получение списка друзей для пользователя с id = " + id);
        User user = findById(id);
        Set<Long> friendIds = user.getFriends();
        Collection<User> friends = friendIds.stream()
                .map(userStorage::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

        log.info("Найдено " + friends.size() + " друзей для пользователя с id = " + id);
        return friends;
    }

    //получение общих друзей двух пользователей
    public Collection<User> findCommonFriends(long id, long otherId) {
        log.info("Запрос на получение списка общих друзей для пользователей с id = " + id + " и id = " + otherId);

        Collection<User> friendsOfUser1 = findAllFriends(id);
        Collection<User> friendsOfUser2 = findAllFriends(otherId);
        friendsOfUser1.retainAll(friendsOfUser2);

        log.info("Найдено " + friendsOfUser1.size() + " общих друзей для пользователей с id = " + id + " и id = " + otherId);
        return friendsOfUser1;
    }
}
