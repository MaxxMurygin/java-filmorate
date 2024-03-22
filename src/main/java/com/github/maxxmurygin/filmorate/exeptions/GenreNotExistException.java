package com.github.maxxmurygin.filmorate.exeptions;

public class GenreNotExistException extends RuntimeException {
    public GenreNotExistException(String message) {
        super(message);
    }
}
