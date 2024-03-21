package com.github.maxxmurygin.filmorate.repository.Db;

import com.github.maxxmurygin.filmorate.model.Film;
import com.github.maxxmurygin.filmorate.model.Mpa;
import com.github.maxxmurygin.filmorate.repository.FilmRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class H2FilmRepositoryTest {
    private final JdbcTemplate jdbcTemplate;
    private FilmRepository filmRepository;
    Film film;

    @BeforeEach
    void init() {
        filmRepository = new H2FilmRepository(jdbcTemplate);
        film = Film.builder()
                .name("Film1")
                .releaseDate(LocalDate.of(2023, 1, 1))
                .duration(150)
                .mpa(Mpa.builder()
                        .id(2)
                        .name("PG")
                        .build())
                .build();
    }

    @Test
    void create() {
        filmRepository.create(film);
        Film stored = filmRepository.findById(film.getId()).orElse(null);

        assertEquals(film, stored);

    }

    @Test
    void update() {
        filmRepository.create(film);
        film.setDescription("Description");
        filmRepository.update(film);
        Film stored = filmRepository.findById(film.getId()).orElse(null);

        assertEquals(stored.getDescription(), "Description");
    }

    @Test
    void findAll() {
        filmRepository.create(film);
        film.setName("Film2");
        filmRepository.create(film);
        film.setName("Film3");
        filmRepository.create(film);

        List<Film> list = filmRepository.findAll();

        assertEquals(list.size(), 3);
    }
}