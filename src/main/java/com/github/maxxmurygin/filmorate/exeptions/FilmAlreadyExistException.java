package com.github.maxxmurygin.filmorate.exeptions;

public class FilmAlreadyExistException extends RuntimeException {
    public FilmAlreadyExistException(String message) {
        super(message);
    }
}
