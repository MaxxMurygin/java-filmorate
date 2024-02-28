package com.github.maxxmurygin.filmorate.storage.user;

import com.github.maxxmurygin.filmorate.exeptions.StorageException;
import com.github.maxxmurygin.filmorate.exeptions.UserNotExistException;
import com.github.maxxmurygin.filmorate.exeptions.UserValidationException;
import com.github.maxxmurygin.filmorate.model.User;
import com.github.maxxmurygin.filmorate.validators.UserValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage{

    private final HashMap<Integer, User> users = new HashMap<>();
    private Integer id = 0;

    @Override
    public User create(User user) {
        Integer id = generateId();
        user.setId(id);
        if (user.getFriends() == null) {
            user.setFriends(new HashSet<>());
        }
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
        stored.setFriends(user.getFriends());
        log.debug("Пользователь {} с ID {} обновлен ", user.getLogin(), user.getId());
        return stored;
    }

    @Override
    public User remove(Integer id) {
        return null;
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
        Set<Integer> userFriendsIds = users.get(userId).getFriends();
        Set<Integer> friendFriendsIds = users.get(friendId).getFriends();

        if ((userFriendsIds == null) || (friendFriendsIds == null)) {
            throw new StorageException("Невозможно получить список друзей");
        }
        userFriendsIds.add(friendId);
//        u.setFriends(userFriendsIds);
        friendFriendsIds.add(userId);
//        f.setFriends(friendFriendsIds);
        return u;
    }

    @Override
    public User removeFriend(Integer userId, Integer friendId) {
        return null;
    }

    private Integer generateId() {
        return ++id;
    }
}
