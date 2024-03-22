package com.github.maxxmurygin.filmorate.exeptions;

public class LikeAlreadyExistException extends RuntimeException {
    public LikeAlreadyExistException(String message) {
        super(message);
    }
}
