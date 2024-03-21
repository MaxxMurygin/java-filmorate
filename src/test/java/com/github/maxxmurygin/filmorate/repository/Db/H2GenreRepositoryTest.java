package com.github.maxxmurygin.filmorate.repository.Db;

import com.github.maxxmurygin.filmorate.model.Genres;
import com.github.maxxmurygin.filmorate.repository.GenreRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class H2GenreRepositoryTest {
    private final JdbcTemplate jdbcTemplate;
    GenreRepository genreRepository;

    @BeforeEach
    void init() {
        genreRepository = new H2GenreRepository(jdbcTemplate);
    }

    @Test
    void findById() {
        Genres genre = genreRepository.findById(2);

        assertEquals(genre.getName(), "Драма");
    }

    @Test
    void findAll() {
        List<Genres> genres = genreRepository.findAll();

        assertEquals(genres.size(), 6);
    }
}