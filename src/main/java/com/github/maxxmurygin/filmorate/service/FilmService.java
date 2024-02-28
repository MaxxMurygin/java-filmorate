package com.github.maxxmurygin.filmorate.service;

import com.github.maxxmurygin.filmorate.exeptions.FilmAlreadyExistException;
import com.github.maxxmurygin.filmorate.exeptions.FilmNotExistException;
import com.github.maxxmurygin.filmorate.exeptions.UserNotExistException;
import com.github.maxxmurygin.filmorate.model.Film;
import com.github.maxxmurygin.filmorate.model.User;
import com.github.maxxmurygin.filmorate.storage.film.FilmStorage;
import com.github.maxxmurygin.filmorate.validators.FilmValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.nio.file.FileAlreadyExistsException;
import java.util.Collection;

@Service
@Slf4j
public class FilmService {

    private final FilmStorage storage;
    private final FilmValidator validator = new FilmValidator();

    @Autowired
    public FilmService(FilmStorage storage) {
        this.storage = storage;
    }

    public Film create(Film film) {
        Film existing = storage.findById(film.getId());

        if (existing != null) {
            throw new FilmAlreadyExistException(String.format(
                    "Фильм %s с ID %d уже существует", film.getName(), film.getId()));
        }
        validator.validate(film);
        return storage.create(film);
    }

    public Film update(Film film) {
        Film stored = storage.findById(film.getId());

        if (stored == null) {
            throw new FilmNotExistException(String.format(
                    "Фильма %s с ID %d не существует", film.getName(), film.getId()));
        }
        validator.validate(film);
        return storage.update(film);
    }

    public Film remove(Integer id) {
        return storage.remove(id);
    }

    public Collection<Film> findAll() {
        return storage.findAll();
    }

    public Film findById(Integer id) {
        Film f = storage.findById(id);
        if (f == null) {
            throw new FilmNotExistException("Фильм не найден");
        }
        return f;
    }

}
