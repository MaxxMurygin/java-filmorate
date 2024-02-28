package com.github.maxxmurygin.filmorate.storage.film;

import com.github.maxxmurygin.filmorate.model.Film;

import java.util.Collection;
import java.util.List;

public interface FilmStorage {

    Film create(Film film);
    Film update(Film film);
    Film remove(Integer id);
    Film findById(Integer id);
    Collection<Film> findAll();
}
