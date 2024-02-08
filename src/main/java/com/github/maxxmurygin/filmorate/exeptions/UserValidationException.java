package com.github.maxxmurygin.filmorate.exeptions;

public class UserValidationException extends RuntimeException {
    public UserValidationException(String message) {
        super(message);
    }
}
