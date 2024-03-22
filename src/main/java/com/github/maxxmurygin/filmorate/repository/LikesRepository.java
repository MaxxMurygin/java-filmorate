package com.github.maxxmurygin.filmorate.repository;

public interface LikesRepository {
    void addLike(Integer filmId, Integer userId);

    void removeLike(Integer filmId, Integer userId);
}
