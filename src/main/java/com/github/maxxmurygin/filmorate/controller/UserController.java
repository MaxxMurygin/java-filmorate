package com.github.maxxmurygin.filmorate.controller;

import com.github.maxxmurygin.filmorate.model.User;
import com.github.maxxmurygin.filmorate.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public Collection<User> findAll() {
        return userService.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
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
    public void addFriend(@PathVariable Integer userId,
                          @PathVariable Integer friendId) {
        userService.addFriend(userId, friendId);
    }

    @DeleteMapping("/{userId}/friends/{friendId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeFriend(@PathVariable Integer userId,
                             @PathVariable Integer friendId) {
        userService.removeFriend(userId, friendId);
    }

    @GetMapping("/{userId}/friends/common/{otherId}")
    public List<User> findCommonFriends(@PathVariable Integer userId,
                                        @PathVariable Integer otherId) {
        return userService.findCommonFriends(userId, otherId);
    }
}