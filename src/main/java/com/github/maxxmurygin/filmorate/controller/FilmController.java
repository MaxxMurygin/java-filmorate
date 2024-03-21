package com.github.maxxmurygin.filmorate.controller;

import com.github.maxxmurygin.filmorate.model.Film;
import com.github.maxxmurygin.filmorate.model.Genres;
import com.github.maxxmurygin.filmorate.model.Mpa;
import com.github.maxxmurygin.filmorate.service.FilmService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequestMapping
@Slf4j
@RequiredArgsConstructor
public class FilmController {
    private final FilmService filmService;

    @GetMapping("/films")
    public Collection<Film> findAll() {
        return filmService.findAll();
    }

    @PostMapping("/films")
    @ResponseStatus(HttpStatus.CREATED)
    public Film create(@Valid @RequestBody Film film) {
        return filmService.create(film);
    }

    @PutMapping("/films")
    public Film update(@Valid @RequestBody Film film) {
        return filmService.update(film);
    }

    @GetMapping("/films/{filmId}")
    public Film findById(@PathVariable int filmId) {
        return filmService.findById(filmId);
    }

    @PutMapping("/films/{filmId}/like/{userId}")
    public Film addLike(@PathVariable int filmId,
                        @PathVariable int userId) {
        return filmService.addLike(filmId, userId);
    }

    @DeleteMapping("/films/{filmId}/like/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeLike(@PathVariable int filmId,
                         @PathVariable int userId) {
        filmService.removeLike(filmId, userId);
    }

    @GetMapping("/films/popular")
    public Collection<Film> getPopular(@RequestParam(defaultValue = "10", required = false) Integer count) {
        return filmService.getPopular(count);
    }

    @GetMapping("/genres")
    public Collection<Genres> findGenresAll() {
        return filmService.findGenreAll();
    }

    @GetMapping("/genres/{genreId}")
    public Genres findGenreById(@PathVariable int genreId) {
        return filmService.findGenreById(genreId);
    }

    @GetMapping("/mpa")
    public Collection<Mpa> findMpaAll() {
        return filmService.findMpaAll();
    }

    @GetMapping("/mpa/{MpaId}")
    public Mpa findMpaById(@PathVariable int MpaId) {
        return filmService.findMpaById(MpaId);
    }
}