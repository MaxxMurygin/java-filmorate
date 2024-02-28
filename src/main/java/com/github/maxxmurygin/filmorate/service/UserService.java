package com.github.maxxmurygin.filmorate.service;

import com.github.maxxmurygin.filmorate.exeptions.FilmAlreadyExistException;
import com.github.maxxmurygin.filmorate.exeptions.UserAlreadyExistException;
import com.github.maxxmurygin.filmorate.exeptions.UserNotExistException;
import com.github.maxxmurygin.filmorate.model.Film;
import com.github.maxxmurygin.filmorate.model.User;
import com.github.maxxmurygin.filmorate.storage.user.UserStorage;
import com.github.maxxmurygin.filmorate.validators.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserStorage storage;
    private final UserValidator validator = new UserValidator();

    @Autowired
    public UserService(UserStorage storage) {
        this.storage = storage;
    }

    public User create(User user) {
        User existing = storage.findByEmail(user.getEmail());

        if (existing != null) {
            throw new UserAlreadyExistException(String.format(
                    "Пользователь c ID = %d с Email %s уже существует", existing.getId(), existing.getEmail()));
        }
        validator.validate(user);
        return storage.create(user);
    }

    public User update(User user) {
        User stored = storage.findById(user.getId());

        if (stored == null) {
            throw new UserNotExistException(String.format(
                    "Пользователя c ID = %d с Email %s не существует", user.getId(), user.getEmail()));
        }
        validator.validate(user);
        return storage.update(user);
    }

    public User remove(Integer id) {
        return storage.remove(id);
    }

    public Collection<User> findAll() {
        return storage.findAll();
    }

    public User findById(Integer userId) {
        User u = storage.findById(userId);
        if (u == null) {
            throw new UserNotExistException(String.format(
                    "Пользователь c ID = %d не найден", userId));
        }
        return u;
    }

    public User addFriend(Integer userId, Integer friendId) {
        User u = storage.findById(userId);
        User f = storage.findById(friendId);
        if (u == null) {
            throw new UserNotExistException(String.format(
                    "Пользователь c ID = %d не найден", userId));
        }
        if (f == null) {
            throw new UserNotExistException(String.format(
                    "Пользователь c ID = %d не найден", friendId));
        }
        return storage.addFriend(userId, friendId);
    }

    public List<User> findFriends(Integer id) {
        User u = storage.findById(id);
        if (u == null) {
            throw new UserNotExistException("Пользователь не найден");
        }
        Set<Integer> friendsId = u.getFriends();
        if (friendsId == null) {
            return new ArrayList<>();
        }
        return u.getFriends()
                .stream()
                .map(storage::findById)
                .collect(Collectors.toList());
    }

    public User removeFriend(Integer userId, Integer friendId) {
        Set<Integer> userFriendsIds;
        Set<Integer> friendFriendsIds;
        User u = storage.findById(userId);
        User f = storage.findById(friendId);

        if ((u == null) || (f == null)) {
            throw new UserNotExistException("Пользователь не найден");
        }
        if (u.getFriends() == null) {
            userFriendsIds = new HashSet<>();
        } else {
            userFriendsIds = new HashSet<>(Set.copyOf(u.getFriends()));
        }
        if (f.getFriends() == null) {
            friendFriendsIds = new HashSet<>();
        } else {
            friendFriendsIds = new HashSet<>(Set.copyOf(f.getFriends()));
        }
        userFriendsIds.remove(friendId);
        u.setFriends(userFriendsIds);
        friendFriendsIds.remove(userId);
        f.setFriends(friendFriendsIds);
        return u;
    }
}
