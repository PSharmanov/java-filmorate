package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final UserService userService;

    @GetMapping
    public Collection<User> findAll() {
        return userService.findAll();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        return userService.create(user);
    }

    @PutMapping
    public User update(@Valid @RequestBody User newUser) {
        return userService.update(newUser);
    }

    // получение пользователя по id
    @GetMapping("/{id}")
    public User findById(@PathVariable("id") long userId) {
        return userService.findById(userId);
    }

    // добавление в друзья
    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable("id") Long id, @PathVariable("friendId") Long friendId) {
        userService.addFriend(id, friendId);
    }

    // удаление из друзей
    @DeleteMapping("/{id}/friends/{friendId}")
    public void removeFriend(@PathVariable("id") Long id, @PathVariable("friendId") Long friendId) {
        userService.removeFriend(id, friendId);
    }

    // получение списка друзей
    @GetMapping("/{id}/friends")
    public Collection<User> findAllFriends(@PathVariable("id") Long id) {
        return userService.findAllFriends(id);
    }

    // получение списка общих друзей с другим пользователем
    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<User> findCommonFriends(@PathVariable("id") Long id, @PathVariable("otherId") Long otherId) {
        return userService.findCommonFriends(id, otherId);
    }

}