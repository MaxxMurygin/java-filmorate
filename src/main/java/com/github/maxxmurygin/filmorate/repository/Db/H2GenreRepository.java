package com.github.maxxmurygin.filmorate.repository.Db;

import com.github.maxxmurygin.filmorate.model.Genres;
import com.github.maxxmurygin.filmorate.repository.GenreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Repository
@RequiredArgsConstructor
@Primary
@Slf4j
public class H2GenreRepository implements GenreRepository {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void addToFilm(int filmId, Set<Genres> genres) {
        String sql = "INSERT INTO PUBLIC.FILM_GENRE (FILM_ID, GENRE_ID) VALUES (?, ?)";

        for (Genres genre : genres) {
            jdbcTemplate.update(sql, filmId, genre.getId());
        }
    }

    @Override
    public List<Genres> findByFilm(int filmId) {
        String sql = "SELECT fg.GENRE_ID, g.GENRE_NAME " +
                "FROM PUBLIC.FILM_GENRE fg " +
                "JOIN PUBLIC.GENRES g " +
                "ON fg.GENRE_ID = g.GENRE_ID " +
                "WHERE fg.FILM_ID = ?";

        try {
            return jdbcTemplate.query(sql, new GenreRowMapper(), filmId);
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<>();
        }
    }

    @Override
    public Genres findById(int id) {
        String sql = "SELECT GENRE_ID, GENRE_NAME " +
                "FROM PUBLIC.GENRES " +
                "WHERE GENRE_ID = ?";

        try {
            return jdbcTemplate.queryForObject(sql, new GenreRowMapper(), id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<Genres> findAll() {
        String sql = "SELECT GENRE_ID, GENRE_NAME " +
                "FROM PUBLIC.GENRES";

        try {
            return jdbcTemplate.query(sql, new GenreRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<>();
        }
    }

    private static class GenreRowMapper implements RowMapper<Genres> {
        @Override
        public Genres mapRow(ResultSet rs, int rowNum) throws SQLException {
            return Genres.builder()
                    .id(rs.getInt("GENRE_ID"))
                    .name(rs.getString("GENRE_NAME"))
                    .build();
        }
    }
}
