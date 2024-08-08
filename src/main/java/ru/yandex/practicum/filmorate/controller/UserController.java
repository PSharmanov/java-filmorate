package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.DuplicatedDataException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.Instant;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final Map<Long, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> findAll() {
        log.info("Запрос на получение списка всех пользователей");
        return users.values();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        log.info("Запрос на создание пользователя");

        List<String> listEmail = users.values()
                .stream()
                .map(User::getEmail)
                .toList();

        if (listEmail.contains(user.getEmail())) {
            log.warn("Имейл" + user.getEmail() + " уже используется");
            throw new DuplicatedDataException("Этот имейл уже используется");
        }

        if (user.getName() == null || user.getName().isBlank()) {
            log.debug("Имя пользователя не заполнено, в место имени использован логин.");
            user.setName(user.getLogin());
        }

        user.setId(getNextId());
        user.setRegistrationDate(Instant.now());
        users.put(user.getId(), user);

        log.info("Добавлен пользователь с id= " + user.getId());

        return user;
    }

    @PutMapping
    public User update(@Valid @RequestBody User newUser) {
        log.info("Обновление данных пользователя c id " + newUser.getId());

        if (newUser.getId() == null) {
            log.warn("В запросе на обновление не указан id");
            throw new ValidationException("Id должен быть указан");
        }

        if (users.containsKey(newUser.getId())) {

            User oldUser = users.get(newUser.getId());

            List<String> listEmail = users.values()
                    .stream()
                    .map(User::getEmail)
                    .toList();

            if (listEmail.contains(newUser.getEmail())) {
                log.warn("В запросе на обновление указан используемый email");
                throw new DuplicatedDataException("Этот имейл уже используется");
            }

            oldUser.setEmail(newUser.getEmail());
            oldUser.setLogin(newUser.getLogin());
            oldUser.setName(newUser.getName());
            oldUser.setBirthday(newUser.getBirthday());
            users.put(newUser.getId(), oldUser);
            log.info("Обновлены данные пользователя с id " + oldUser.getId());

            return oldUser;
        }

        log.warn("Обновление данных пользователя не произошло, не найден пользователь с id " + newUser.getId());
        throw new NotFoundException("User с id = " + newUser.getId() + " не найден");

    }

    // вспомогательный метод для генерации идентификатора нового пользователя
    private Long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}