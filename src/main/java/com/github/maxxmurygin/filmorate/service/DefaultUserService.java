package com.github.maxxmurygin.filmorate.service;

import com.github.maxxmurygin.filmorate.exeptions.UserAlreadyExistException;
import com.github.maxxmurygin.filmorate.exeptions.UserNotExistException;
import com.github.maxxmurygin.filmorate.model.User;
import com.github.maxxmurygin.filmorate.repository.FilmRepository;
import com.github.maxxmurygin.filmorate.repository.FriendsRepository;
import com.github.maxxmurygin.filmorate.repository.UserRepository;
import com.github.maxxmurygin.filmorate.validators.UserValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
        User existing = userRepository.findByEmail(user.getEmail());

        if (existing != null) {
            log.debug(existing.getEmail());
            throw new UserAlreadyExistException(String.format(
                    "Пользователь c ID = %d с Email %s уже существует", existing.getId(), existing.getEmail()));
        }
        validator.validate(user);
        return userRepository.create(user);
    }

    @Override
    public User update(User user) {
        User stored = userRepository.findById(user.getId());

        if (stored == null) {
            throw new UserNotExistException(String.format(
                    "Пользователя c ID = %d с Email %s не существует", user.getId(), user.getEmail()));
        }
        validator.validate(user);
        return userRepository.update(user);
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
    public User addFriend(Integer userId, Integer friendId) {
        return userRepository.addFriend(userId, friendId);
    }

    @Override
    public User removeFriend(Integer userId, Integer friendId) {
        return userRepository.removeFriend(userId, friendId);
    }

    @Override
    public List<User> findFriends(Integer id) {
        User u = userRepository.findById(id);

        if (u == null) {
            throw new UserNotExistException("Пользователь не найден");
        }
        return friendsRepository.findFriends(id)
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
