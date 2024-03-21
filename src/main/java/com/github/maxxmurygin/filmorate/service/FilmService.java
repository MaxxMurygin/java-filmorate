package com.github.maxxmurygin.filmorate.service;

import com.github.maxxmurygin.filmorate.model.Film;
import com.github.maxxmurygin.filmorate.model.Genres;
import com.github.maxxmurygin.filmorate.model.Mpa;

import java.util.List;

public interface FilmService {
    Film create(Film film);

    Film update(Film film);

    Film addLike(Integer filmId, Integer userId);

    void removeLike(Integer filmId, Integer userId);

    List<Film> findAll();

    Film findById(Integer id);

    Genres findGenreById(Integer id);

    List<Genres> findGenreAll();

    Mpa findMpaById(Integer id);

    List<Mpa> findMpaAll();

    List<Film> getPopular(Integer count);
}
