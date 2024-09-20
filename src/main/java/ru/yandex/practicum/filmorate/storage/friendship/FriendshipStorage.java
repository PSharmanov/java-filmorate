package ru.yandex.practicum.filmorate.storage.friendship;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface FriendshipStorage {
    //добавление в друзья
    void addFriend(Long userId, Long friendId);

    //удаление из друзей
    void removeFriend(Long userId, Long friendId);

    //получение всех друзей
    Collection<User> findAllFriends(Long id);

    //получение общих друзей
    Collection<User> findCommonFriends(Long id, Long otherId);
}
