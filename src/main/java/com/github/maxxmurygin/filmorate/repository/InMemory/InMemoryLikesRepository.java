package com.github.maxxmurygin.filmorate.repository.InMemory;

import com.github.maxxmurygin.filmorate.repository.LikesRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@Repository
@Slf4j
public class InMemoryLikesRepository implements LikesRepository {
    private final HashMap<Integer, Set<Integer>> likes = new HashMap<>();

    @Override
    public void addLike(Integer filmId, Integer userId) {
        if (!likes.containsKey(filmId)) {
            likes.put(filmId, new HashSet<>());
        }
        likes.get(filmId).add(userId);
    }

    @Override
    public void removeLike(Integer filmId, Integer userId) {
        if (!likes.containsKey(filmId)) {
            likes.put(filmId, new HashSet<>());
        }
        likes.get(filmId).add(userId);
    }
}
