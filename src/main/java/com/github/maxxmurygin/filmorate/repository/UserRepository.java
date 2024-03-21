package com.github.maxxmurygin.filmorate.repository;

import com.github.maxxmurygin.filmorate.model.User;

import java.util.Collection;
import java.util.Optional;

public interface UserRepository {
    User create(User user);

    User update(User user);

    Optional<User> findByEmail(String email);

    Optional<User> findByLogin(String login);

    Optional<User> findById(Integer id);

    Collection<User> findAll();
}
