package com.github.maxxmurygin.filmorate.storage.film;

import com.github.maxxmurygin.filmorate.exeptions.FilmNotExistException;
import com.github.maxxmurygin.filmorate.model.Film;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

@Repository
@Slf4j
public class InMemoryFilmStorage implements FilmStorage{
    private final HashMap<Integer, Film> films = new HashMap<>();
    private Integer id = 0;

    @Override
    public Film create(Film film) {
        Integer id = generateId();
        film.setId(id);
        film.setLikes(new HashSet<>());
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
    public Film findById(Integer id) {
        return films.get(id);
    }

    @Override
    public Collection<Film> findAll() {
        return films.values();
    }

    @Override
    public Film like(Integer filmId, Integer userId) {
        Film f = films.get(filmId);

        if (f == null) {
            throw new FilmNotExistException(String.format(
                    "Фильм c ID = %d не найден", filmId));
        }
        f.getLikes().add(userId);
        return f;
    }

    @Override
    public Film dislike(Integer filmId, Integer userId) {
        Film f = films.get(filmId);

        if (f == null) {
            throw new FilmNotExistException(String.format(
                    "Фильм c ID = %d не найден", filmId));
        }
        f.getLikes().remove(userId);
        return f;
    }

    private Integer generateId() {
        return ++id;
    }
}
