package com.github.maxxmurygin.filmorate.exeptions;

public class UserNotExistException extends RuntimeException {
    public UserNotExistException(String message) {
        super(message);
    }
}
