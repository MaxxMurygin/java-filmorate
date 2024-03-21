package com.github.maxxmurygin.filmorate.repository.InMemory;

import com.github.maxxmurygin.filmorate.model.Film;
import com.github.maxxmurygin.filmorate.repository.FilmRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Repository
@Slf4j
public class InMemoryFilmRepository implements FilmRepository {
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
    public Optional<Film> findById(Integer id) {
        return Optional.ofNullable(films.get(id));
    }

    @Override
    public List<Film> findAll() {
        return new ArrayList<>(films.values());
    }

    @Override
    public List<Film> getPopular(Integer limit) {
        return new ArrayList<>();
    }

    private Integer generateId() {
        return ++id;
    }
}
