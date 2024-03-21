package com.github.maxxmurygin.filmorate.repository.Db;

import com.github.maxxmurygin.filmorate.model.Mpa;
import com.github.maxxmurygin.filmorate.model.User;
import com.github.maxxmurygin.filmorate.repository.MpaRepository;
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
class H2MpaRepositoryTest {
    private final JdbcTemplate jdbcTemplate;
    private MpaRepository mpaRepository;


    @BeforeEach
    void init() {
        mpaRepository = new H2MpaRepository(jdbcTemplate);
    }

    @Test
    void findById() {
        Mpa mpa = mpaRepository.findById(3);
        assertEquals(mpa.getName(), "PG-13");
    }

    @Test
    void findAll() {
        List<Mpa> allMpa = mpaRepository.findAll();
        assertEquals(allMpa.size(), 5);
    }
}