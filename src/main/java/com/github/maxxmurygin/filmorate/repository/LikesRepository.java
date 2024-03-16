package com.github.maxxmurygin.filmorate.repository;

import java.util.List;

public interface LikesRepository {
    void addLike(Integer filmId, Integer userId);
    void removeLike(Integer filmId, Integer userId);
    List<Integer> getPopular(Integer limit);
}
