package com.github.maxxmurygin.filmorate.controller;

import com.github.maxxmurygin.filmorate.exeptions.FilmValidationException;
import com.github.maxxmurygin.filmorate.model.Film;
import com.github.maxxmurygin.filmorate.validators.FilmValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final HashMap<Integer, Film> films = new HashMap<>();
    private final FilmValidator validator = new FilmValidator();
    private int id = 0;

    @GetMapping
    public List<Film> findAll() {
        return new ArrayList<>(films.values());
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        if (films.containsKey(film.getId())) {
            log.error("Фильм {} с ID {} уже существует", film.getName(), film.getId());
            return film;
        }
        film.setId(generateId());
        try {
            validator.validate(film);
        } catch (FilmValidationException e) {
            log.error(e.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Ошибка валидации фильма ", e);
        }
        log.debug("Фильм {} ID {} создан", film.getName(), film.getId());
        films.put(film.getId(), film);
        return film;
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        Film stored = films.get(film.getId());
        if (stored == null) {
            log.error("Фильма {} ID {} нее существует", film.getName(), film.getId());
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Фильма не существует ");
        }
        try {
            validator.validate(film);
        } catch (FilmValidationException e) {
            log.error(e.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Ошибка валидации фильма", e);
        }
        stored.setDuration(film.getDuration());
        stored.setReleaseDate(film.getReleaseDate());
        stored.setName(film.getName());
        stored.setDescription(film.getDescription());
        log.debug("Фильм {} обновлен", film.getId());
        return film;
    }

    private int generateId() {
        return ++id;
    }
}
