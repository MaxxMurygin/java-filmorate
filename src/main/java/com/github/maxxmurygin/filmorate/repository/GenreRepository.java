package com.github.maxxmurygin.filmorate.repository;

import com.github.maxxmurygin.filmorate.model.Genres;

import java.util.List;
import java.util.Set;

public interface GenreRepository {
    void addToFilm(int filmId, Set<Genres> genres);

    List<Genres> findByFilm(int filmId);

    Genres findById(int id);

    List<Genres> findAll();
}
