package com.github.maxxmurygin.filmorate.validators;

import com.github.maxxmurygin.filmorate.exeptions.UserValidationException;
import com.github.maxxmurygin.filmorate.model.User;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

@Component
public class UserValidator implements Validator<User> {
    @Override
    public void validate(@NotNull User user) {
        String name = user.getName();
        String login = user.getLogin();
        if (login.contains(" ")) {
            throw new UserValidationException("Недопустимый логин ");
        }
        if (name == null) {
            user.setName(login);
        } else {
            if (name.isBlank() || name.isEmpty()) {
                user.setName(login);
            }
        }

    }
}
