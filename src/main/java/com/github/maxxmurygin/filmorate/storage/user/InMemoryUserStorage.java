package com.github.maxxmurygin.filmorate.storage.user;

import com.github.maxxmurygin.filmorate.exeptions.UserNotExistException;
import com.github.maxxmurygin.filmorate.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {

    private final HashMap<Integer, User> users = new HashMap<>();
    private Integer id = 0;

    @Override
    public User create(User user) {
        Integer id = generateId();
        user.setId(id);
        user.setFriends(new HashSet<>());
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
    public User findByEmail(String email) {
        return null;
    }

    @Override
    public User findById(Integer id) {
        return users.get(id);
    }

    @Override
    public Collection<User> findAll() {
        return users.values();
    }

    @Override
    public User addFriend(Integer userId, Integer friendId) {
        User u = users.get(userId);
        User f = users.get(friendId);

        if (u == null) {
            throw new UserNotExistException(String.format(
                    "Пользователь c ID = %d не найден", userId));
        }
        if (f == null) {
            throw new UserNotExistException(String.format(
                    "Пользователь c ID = %d не найден", friendId));
        }
        u.getFriends().add(f.getId());
        f.getFriends().add(u.getId());
        return u;
    }

    @Override
    public User removeFriend(Integer userId, Integer friendId) {
        User u = users.get(userId);
        User f = users.get(friendId);

        if (u == null) {
            throw new UserNotExistException(String.format(
                    "Пользователь c ID = %d не найден", userId));
        }
        if (f == null) {
            throw new UserNotExistException(String.format(
                    "Пользователь c ID = %d не найден", friendId));
        }
        u.getFriends().add(friendId);
        f.getFriends().add(userId);
        return u;
    }

    private Integer generateId() {
        return ++id;
    }
}
