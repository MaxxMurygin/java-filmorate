package com.github.maxxmurygin.filmorate.service;

import com.github.maxxmurygin.filmorate.model.Film;

import java.util.Collection;

public interface FilmService {
    Film create(Film film);

    Film update(Film film);

    Film addLike(Integer filmId, Integer userId);

    Film removeLike(Integer filmId, Integer userId);

    Collection<Film> findAll();

    Film findById(Integer id);

    Collection<Film> getPopular(Integer count);
}
