package com.github.maxxmurygin.filmorate.storage.film;

import com.github.maxxmurygin.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {

    Film create(Film film);
    Film update(Film film);

    Film findById(Integer id);
    Collection<Film> findAll();
    Film like(Integer filmId, Integer userId);
    Film dislike(Integer filmId, Integer userId);
}
