package com.github.maxxmurygin.filmorate.repository.InMemory;

import com.github.maxxmurygin.filmorate.model.User;
import com.github.maxxmurygin.filmorate.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.HashMap;
import java.util.Optional;

@Repository
//@Primary
@Slf4j
public class InMemoryUserRepository implements UserRepository {
    private final HashMap<Integer, User> users = new HashMap<>();
    private Integer id = 0;

    @Override
    public User create(User user) {
        Integer id = generateId();
        user.setId(id);
        users.put(id, user);
        log.debug("Пользователь {} создан ", user.getLogin());
        return user;
    }

    @Override
    public User update(User user) {
        User stored = users.get(user.getId());

        stored.setEmail(user.getEmail());
        stored.setLogin(user.getLogin());
        stored.setName(user.getName());
        stored.setBirthday(user.getBirthday());
        log.debug("Пользователь {} с ID {} обновлен ", user.getLogin(), user.getId());
        return stored;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return users.values()
                .stream()
                .filter(u -> u.getEmail().equals(email))
                .findFirst();
    }

    @Override
    public Optional<User> findByLogin(String login) {
        return users.values()
                .stream()
                .filter(u -> u.getLogin().equals(login))
                .findFirst();
    }

    @Override
    public Optional<User> findById(Integer id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public Collection<User> findAll() {
        return users.values();
    }

    private Integer generateId() {
        return ++id;
    }
}
