package com.github.maxxmurygin.filmorate.service;

import com.github.maxxmurygin.filmorate.exeptions.UserAlreadyExistException;
import com.github.maxxmurygin.filmorate.exeptions.UserNotExistException;
import com.github.maxxmurygin.filmorate.model.User;
import com.github.maxxmurygin.filmorate.repository.FriendsRepository;
import com.github.maxxmurygin.filmorate.repository.UserRepository;
import com.github.maxxmurygin.filmorate.validators.UserValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Primary
@Slf4j
@RequiredArgsConstructor
public class DefaultUserService implements UserService {
    private final UserRepository userRepository;
    private final FriendsRepository friendsRepository;
    private final UserValidator validator;

    @Override
    public User create(User user) {
//        if ( userRepository.findByLogin(user.getLogin()) != null) {
//            throw new UserAlreadyExistException(String.format(
//                    "Пользователь с login = %s уже существует", user.getLogin()));
//        }
//        if (userRepository.findByEmail(user.getEmail()) != null) {
//            throw new UserAlreadyExistException(String.format(
//                    "Пользователь с email = %s уже существует", user.getEmail()));
//        }

        validator.validate(user);
        log.debug("DefaultUserService: Создаем пользователя {}", user.getLogin());
        User u = userRepository.create(user);
        log.debug("DefaultUserService: Создан пользователь {}", u.getLogin());
        return u;
    }

    @Override
    public User update(User user) {
//        User stored = userRepository.findById(user.getId());
//
//        if (stored == null) {
//            throw new UserNotExistException(String.format(
//                    "Пользователя c ID = %d с Email %s не существует", user.getId(), user.getEmail()));
//        }
        validator.validate(user);
        log.debug("Обновляем пользователя {}", user.getLogin());
        User u = userRepository.update(user);
        log.debug("Обновлен пользователь {}", user.getLogin());
        return u;
    }

    @Override
    public Collection<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findById(Integer userId) {
        User u = userRepository.findById(userId);
        if (u == null) {
            throw new UserNotExistException(String.format(
                    "Пользователь c ID = %d не найден", userId));
        }
        return u;
    }

    @Override
    public void addFriend(Integer userId, Integer friendId) {
        User u = userRepository.findById(userId);
        User f = userRepository.findById(friendId);

        if (u == null) {
            throw new UserNotExistException(String.format(
                    "Пользователь c ID = %d не найден", userId));
        }
        if (f == null) {
            throw new UserNotExistException(String.format(
                    "Пользователь c ID = %d не найден", userId));
        }
        friendsRepository.addFriend(userId, friendId);
    }

    @Override
    public void removeFriend(Integer userId, Integer friendId) {
        User u = userRepository.findById(userId);
        User f = userRepository.findById(friendId);

        if (u == null) {
            throw new UserNotExistException(String.format(
                    "Пользователь c ID = %d не найден", userId));
        }
        if (f == null) {
            throw new UserNotExistException(String.format(
                    "Пользователь c ID = %d не найден", userId));
        }
        friendsRepository.removeFriend(userId, friendId);
    }

    @Override
    public List<User> findFriends(Integer userId) {
        User u = userRepository.findById(userId);

        if (u == null) {
            throw new UserNotExistException("Пользователь не найден");
        }
        return friendsRepository.findFriends(userId)
                .stream()
                .map(userRepository::findById)
                .collect(Collectors.toList());
    }

    @Override
    public List<User> findCommonFriends(Integer userId, Integer otherId) {
        return friendsRepository.findCommonFriends(userId, otherId)
                .stream()
                .map(userRepository::findById)
                .collect(Collectors.toList());

    }
}
