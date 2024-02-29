package com.github.maxxmurygin.filmorate.service;

import com.github.maxxmurygin.filmorate.exeptions.UserAlreadyExistException;
import com.github.maxxmurygin.filmorate.exeptions.UserNotExistException;
import com.github.maxxmurygin.filmorate.model.User;
import com.github.maxxmurygin.filmorate.storage.user.UserStorage;
import com.github.maxxmurygin.filmorate.validators.UserValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
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
            log.debug(existing.getEmail());
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
        return storage.addFriend(userId, friendId);
    }

    public User removeFriend(Integer userId, Integer friendId) {
        return storage.removeFriend(userId, friendId);
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

    public List<User> findCommonFriends(Integer userId, Integer otherId) {
        List<Integer> result = new ArrayList<>(storage.findById(userId).getFriends());

        result.retainAll(storage.findById(otherId).getFriends());
        return result
                .stream()
                .map(storage::findById)
                .collect(Collectors.toList());
    }
}
