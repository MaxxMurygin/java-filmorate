package com.github.maxxmurygin.filmorate.service;

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
import java.util.Optional;
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
        validator.validate(user);
        log.debug("DefaultUserService: Создаем пользователя {}", user.getLogin());
        User u = userRepository.create(user);
        log.debug("DefaultUserService: Создан пользователь {}", u.getLogin());
        return u;
    }

    @Override
    public User update(User user) {
        userRepository.findById(user.getId()).orElseThrow(() -> new UserNotExistException(
                String.format("Пользователь c ID = %d не найден", user.getId())));

        validator.validate(user);
        log.debug("DefaultUserService: Обновляем пользователя {}", user.getLogin());
        User u = userRepository.update(user);
        log.debug("DefaultUserService: Обновлен пользователь {}", user.getLogin());
        return u;
    }

    @Override
    public Collection<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findById(Integer userId) {
        return userRepository.findById(userId).orElseThrow(() -> new UserNotExistException(
                String.format("Пользователь c ID = %d не найден", userId)));
    }

    @Override
    public void addFriend(Integer userId, Integer friendId) {
        userRepository.findById(userId).orElseThrow(() -> new UserNotExistException(
                String.format("Пользователь c ID = %d не найден", userId)));
        userRepository.findById(friendId).orElseThrow(() -> new UserNotExistException(
                String.format("Пользователь c ID = %d не найден", friendId)));
        log.debug("DefaultUserService: Добавляем пользователю {} друга {}", userId, friendId);
        friendsRepository.addFriend(userId, friendId);
        log.debug("DefaultUserService: Добавлен пользователю {} друг {}", userId, friendId);
    }

    @Override
    public void removeFriend(Integer userId, Integer friendId) {
        userRepository.findById(userId).orElseThrow(() -> new UserNotExistException(
                String.format("Пользователь c ID = %d не найден", userId)));
        userRepository.findById(friendId).orElseThrow(() -> new UserNotExistException(
                String.format("Пользователь c ID = %d не найден", friendId)));
        friendsRepository.removeFriend(userId, friendId);
    }

    @Override
    public List<User> findFriends(Integer userId) {
        userRepository.findById(userId).orElseThrow(() -> new UserNotExistException(
                String.format("Пользователь c ID = %d не найден", userId)));
        log.debug("DefaultUserService: Ищем друзей пользователя {}", userId);
        List<User> friends = friendsRepository.findFriends(userId)
                .stream()
                .map(userRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
        log.debug("DefaultUserService: Найдены друзья: {}", friends);
        return friends;
    }

    @Override
    public List<User> findCommonFriends(Integer userId, Integer otherId) {
        userRepository.findById(userId).orElseThrow(() -> new UserNotExistException(
                String.format("Пользователь c ID = %d не найден", userId)));
        userRepository.findById(otherId).orElseThrow(() -> new UserNotExistException(
                String.format("Пользователь c ID = %d не найден", otherId)));

        log.debug("DefaultUserService: Ищем друзей пользователей {}, {}", userId, otherId);
        List<User> commonFriends = friendsRepository.findCommonFriends(userId, otherId)
                .stream()
                .map(userRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
        log.debug("DefaultUserService: Найдены друзья: {}", commonFriends);
        return commonFriends;
    }
}
