package com.github.maxxmurygin.filmorate.storage.film;

import com.github.maxxmurygin.filmorate.exeptions.FilmValidationException;
import com.github.maxxmurygin.filmorate.model.Film;
import com.github.maxxmurygin.filmorate.validators.FilmValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import java.util.HashMap;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage{
    private final HashMap<Integer, Film> films = new HashMap<>();
    private Integer id = 0;

    @Override
    public Film create(Film film) {
        Integer id = generateId();
        film.setId(id);
        films.put(id, film);
        log.debug("Фильм {} ID {} создан", film.getName(), film.getId());
        return film;
    }

    @Override
    public Film update(Film film) {
        Film stored = films.get(film.getId());
        stored.setDuration(film.getDuration());
        stored.setReleaseDate(film.getReleaseDate());
        stored.setName(film.getName());
        stored.setDescription(film.getDescription());
        log.debug("Фильм  {}  c ID {} обновлен", film.getName(), film.getId());
        return stored;
    }

    @Override
    public Film remove(Integer id) {
        return films.remove(id);
    }

    @Override
    public Film findById(Integer id) {
        return films.get(id);
    }

    @Override
    public Collection<Film> findAll() {
        return films.values();
    }

    private Integer generateId() {
        return ++id;
    }
}
