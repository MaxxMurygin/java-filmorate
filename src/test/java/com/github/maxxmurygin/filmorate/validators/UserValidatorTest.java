package com.github.maxxmurygin.filmorate.validators;

import com.github.maxxmurygin.filmorate.exeptions.UserValidationException;
import com.github.maxxmurygin.filmorate.model.User;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserValidatorTest {
    private final UserValidator validator = new UserValidator();
    private final User user = User.builder()
            .login("User1")
            .email("user1@userdomain.net")
            .name("User1")
            .birthday(LocalDate.of(2022, 1, 1))
            .build();

    @Test
    void validateOk() {
        validator.validate(user);
    }

    @Test
    void validateWrongLogin() {
        user.setLogin("use r");
        UserValidationException ex = assertThrows(UserValidationException.class,
                () -> validator.validate(user));
        assertEquals("Недопустимый логин ", ex.getMessage());
    }
}