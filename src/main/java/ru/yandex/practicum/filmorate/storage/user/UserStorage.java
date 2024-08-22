package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Optional;

public interface UserStorage {

    //получение всех пользователей
    Collection<User> findAll();

    //получение пользователя по id
    Optional<User> findById(long userId);

    //создание пользователя
    User create(User user);

    //обновление пользователя
    User update(User user);

}