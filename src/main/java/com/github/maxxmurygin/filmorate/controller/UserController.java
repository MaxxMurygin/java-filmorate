package com.github.maxxmurygin.filmorate.controller;

import com.github.maxxmurygin.filmorate.exeptions.UserValidationException;
import com.github.maxxmurygin.filmorate.model.User;
import com.github.maxxmurygin.filmorate.validators.UserValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final HashMap<Integer, User> users = new HashMap<>();
    private final UserValidator validator = new UserValidator();
    private int id = 0;
    @GetMapping
    public List<User> findAll(){
        return new ArrayList<>(users.values());
    }

    @PostMapping
    public User create(@Valid @RequestBody User user){
        if (users.containsKey(user.getId())){
            log.error("Пользователь {} с ID {} уже существует ", user.getLogin(), user.getId());
            return user;
        }
        if (user.getId() == 0){
            user.setId(generateId());
        }

        try {
            validator.validate(user);
        } catch (UserValidationException e){
            log.error(e.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Ошибка валидации ", e);
        }
        log.debug("Пользователь {} создан ", user.getLogin());
        users.put(user.getId(), user);
        return user;
    }

    @PutMapping
    public User update(@Valid @RequestBody User user){
        User stored = users.get(user.getId());

        if (stored == null){
            log.error("Пользователя {} не существует ", user.getLogin());
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Пользователя не существует ");
        }
        try {
            validator.validate(user);
        } catch (UserValidationException e){
            log.error(e.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Ошибка валидации ", e);
        }
        stored.setEmail(user.getEmail());
        stored.setLogin(user.getLogin());
        stored.setName(user.getName());
        stored.setBirthday(user.getBirthday());
        log.debug("Пользователь {} с ID {} обновлен ", user.getLogin(), user.getId());
        return stored;
    }

    @DeleteMapping
    private void deleteAll(){
        users.clear();
    }

    private int generateId(){
        return ++id;
    }
}