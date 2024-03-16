package com.github.maxxmurygin.filmorate.service;

import com.github.maxxmurygin.filmorate.model.Film;

import java.util.Collection;
import java.util.List;

public interface FilmService {
    Film create(Film film);

    Film update(Film film);

    Film addLike(Integer filmId, Integer userId);

    void removeLike(Integer filmId, Integer userId);

    List<Film> findAll();

    Film findById(Integer id);

    List<Film> getPopular(Integer count);
}
