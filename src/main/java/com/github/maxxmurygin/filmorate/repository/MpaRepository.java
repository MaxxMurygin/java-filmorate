package com.github.maxxmurygin.filmorate.repository;

import com.github.maxxmurygin.filmorate.model.Mpa;

import java.util.List;

public interface MpaRepository {
    Mpa findById(int id);
    List<Mpa> findAll();
}
