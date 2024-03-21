package com.github.maxxmurygin.filmorate.repository;

import com.github.maxxmurygin.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmRepository {

    Film create(Film film);
    Film update(Film film);

    Optional<Film> findById(Integer id);
    List<Film> findAll();
    List<Film> getPopular(Integer limit);
}
