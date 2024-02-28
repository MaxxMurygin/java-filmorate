package com.github.maxxmurygin.filmorate.controller;

import com.github.maxxmurygin.filmorate.model.Film;
import com.github.maxxmurygin.filmorate.model.User;
import com.github.maxxmurygin.filmorate.service.FilmService;
import com.github.maxxmurygin.filmorate.storage.film.InMemoryFilmStorage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final FilmService filmService;

    @Autowired
    public FilmController(InMemoryFilmStorage storage) {
        this.filmService = new FilmService(storage);
    }


    @GetMapping
    public Collection<Film> findAll() {
        return filmService.findAll();
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        return filmService.create(film);
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        return filmService.update(film);
    }

    @GetMapping("/{filmId}")
    public Film findById(@PathVariable int filmId) {
        return filmService.findById(filmId);
    }
}
