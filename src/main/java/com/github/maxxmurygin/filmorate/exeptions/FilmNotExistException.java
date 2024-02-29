package com.github.maxxmurygin.filmorate.exeptions;

public class FilmNotExistException extends RuntimeException {
    public FilmNotExistException(String message) {
        super(message);
    }
}
