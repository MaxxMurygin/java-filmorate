package com.github.maxxmurygin.filmorate.storage.user;

import com.github.maxxmurygin.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {

    User create(User user);
    User update(User user);
    User findByEmail(String email);
    User findById(Integer id);
    Collection<User> findAll();
    User addFriend(Integer userId, Integer friendId);
    User removeFriend(Integer userId, Integer friendId);
}
