package com.github.maxxmurygin.filmorate.controller;

import com.github.maxxmurygin.filmorate.exeptions.*;
import com.github.maxxmurygin.filmorate.model.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {
    @ExceptionHandler({FilmValidationException.class, UserValidationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationException(final RuntimeException e) {
        log.debug(e.getMessage());
        return new ErrorResponse(String.format("Ошибка валидации: \"%s\".", e.getMessage()));
    }

    @ExceptionHandler({FilmNotExistException.class, UserNotExistException.class,
            GenreNotExistException.class, MpaNotExistException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotExist(final RuntimeException e) {
        log.debug(e.getMessage());
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler({FilmAlreadyExistException.class, UserAlreadyExistException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleAlreadyExist(final RuntimeException e) {
        log.debug(e.getMessage());
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler({Exception.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleOtherException(final Exception e) {
        log.debug(e.getMessage());
        return new ErrorResponse(e.getMessage());
    }
}
