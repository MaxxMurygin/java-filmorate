package com.github.maxxmurygin.filmorate.repository;

import com.github.maxxmurygin.filmorate.model.User;

import java.util.Collection;
import java.util.List;

public interface UserRepository {

    User create(User user);
    User update(User user);
    User findByEmail(String email);
    User findById(Integer id);
    Collection<User> findAll();
}
