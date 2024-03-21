package com.github.maxxmurygin.filmorate.repository.InMemory;

import com.github.maxxmurygin.filmorate.repository.LikesRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

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

    @Override
    public List<Integer> getPopular(Integer count) {
        return likes.keySet()
                .stream()
                .sorted(Comparator.comparingInt(f -> likes.get(f).size()).reversed())
                .limit(count)
                .collect(Collectors.toList());
    }
}
