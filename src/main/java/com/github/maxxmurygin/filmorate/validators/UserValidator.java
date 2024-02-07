package com.github.maxxmurygin.filmorate.validators;

import com.github.maxxmurygin.filmorate.exeptions.UserValidationException;
import com.github.maxxmurygin.filmorate.model.User;

public class UserValidator implements Validator<User>{
    @Override
    public void validate(User user) {
        String name = user.getName();
        String login = user.getLogin();
        if (name == null){
            user.setName(user.getLogin());
        } else {
            if (name.isBlank() || name.isEmpty()){
                user.setName(user.getLogin());
            }
        }

        if (login.contains(" ")){
            throw new UserValidationException("Недопустимый логин");
        }
    }
}
