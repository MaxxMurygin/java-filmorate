package com.github.maxxmurygin.filmorate.controller;

import com.github.maxxmurygin.filmorate.model.User;
import com.github.maxxmurygin.filmorate.service.UserService;
import com.github.maxxmurygin.filmorate.storage.film.InMemoryFilmStorage;
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
    public UserController(InMemoryUserStorage storage) {
        this.userService = new UserService(storage);
    }


    @GetMapping
    public Collection<User> findAll() {
        return userService.findAll();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        User u = userService.create(user);
        return u;

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
        List<User> df = userService.findFriends(userId);
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
}