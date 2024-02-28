package com.github.maxxmurygin.filmorate.controller;

import com.github.maxxmurygin.filmorate.model.User;
import com.github.maxxmurygin.filmorate.service.UserService;
import com.github.maxxmurygin.filmorate.storage.user.InMemoryUserStorage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(@Autowired InMemoryUserStorage storage) {
        this.userService = new UserService(storage);
    }


    @GetMapping
    public Collection<User> findAll() {
        return userService.findAll();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        return userService.create(user);

    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        return userService.update(user);

    }

    @GetMapping("/{userId}")
    public User findById(@PathVariable Integer userId) {
        return userService.findById(userId);
    }

    @GetMapping("/{userId}/friends")
    public List<User> findFriends(@PathVariable Integer userId) {
        return userService.findFriends(userId);
    }

    @PutMapping("/{userId}/friends/{friendId}")
    public User addFriend(@PathVariable Integer userId,
                          @PathVariable Integer friendId) {
        return userService.addFriend(userId, friendId);
    }

    @DeleteMapping("/{userId}/friends/{friendId}")
    public User removeFriend(@PathVariable Integer userId,
                             @PathVariable Integer friendId) {
        return userService.removeFriend(userId, friendId);
    }

    @GetMapping("/{userId}/friends/common/{otherId}")
    public List<User> findCommonFriends(@PathVariable Integer userId,
                                        @PathVariable Integer otherId) {
        return userService.findCommonFriends(userId, otherId);
    }
}