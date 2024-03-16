package com.github.maxxmurygin.filmorate.repository;

import com.github.maxxmurygin.filmorate.model.Film;

import java.util.Collection;
import java.util.List;

public interface FilmRepository {

    Film create(Film film);
    Film update(Film film);

    Film findById(Integer id);
    List<Film> findAll();
}
