package com.github.maxxmurygin.filmorate.service;

import com.github.maxxmurygin.filmorate.model.User;

import java.util.Collection;
import java.util.List;

public interface UserService {
    User create(User user);

    User update(User user);

    Collection<User> findAll();

    User findById(Integer userId);

    void addFriend(Integer userId, Integer friendId);

    void removeFriend(Integer userId, Integer friendId);

    List<User> findFriends(Integer id);

    List<User> findCommonFriends(Integer userId, Integer otherId);
}
