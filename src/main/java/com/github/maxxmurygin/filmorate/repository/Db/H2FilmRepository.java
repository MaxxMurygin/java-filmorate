package com.github.maxxmurygin.filmorate.repository.Db;

import com.github.maxxmurygin.filmorate.exeptions.FilmValidationException;
import com.github.maxxmurygin.filmorate.model.Film;
import com.github.maxxmurygin.filmorate.model.Mpa;
import com.github.maxxmurygin.filmorate.repository.FilmRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Primary
@Slf4j
public class H2FilmRepository implements FilmRepository {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Film create(Film film) {
        try {
            final String sql = "INSERT INTO PUBLIC.FILMS (NAME, DESCRIPTION, RELEASE_DATE, DURATION, RATING_ID) " +
                    "VALUES(?, ?, ?, ?, ?)";
            KeyHolder keyHolder = new GeneratedKeyHolder();

            log.debug("H2: Film {} start create into DB", film.getName());
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection
                        .prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, film.getName());
                ps.setString(2, film.getDescription());
                ps.setObject(3, film.getReleaseDate());
                ps.setInt(4, film.getDuration());
                ps.setInt(5, film.getMpa().getId());
                return ps;
            }, keyHolder);
            film.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());
            log.debug("H2: Film {} end create into DB", film.getName());
            return film;
        } catch (DataAccessException e) {
            throw new FilmValidationException(e.getMessage());
        }
    }

    @Override
    public Film update(Film film) {
        String sql = "UPDATE PUBLIC.FILMS SET NAME = ?, DESCRIPTION = ?, " +
                "RELEASE_DATE = ?, DURATION = ?, RATING_ID = ? " +
                "WHERE FILM_ID = ?";

        log.debug("H2: Film {} start update into DB", film.getName());
        try {
            jdbcTemplate.update(sql, film.getName(), film.getDescription(),
                    film.getReleaseDate(), film.getDuration(), film.getMpa().getId(), film.getId());
        } catch (DataAccessException e) {
            throw new FilmValidationException(e.getMessage());
        }
        log.debug("H2: Film {} end update into DB", film.getName());
        return film;
    }

    @Override
    public Optional<Film> findById(Integer id) {
        String sql = "SELECT * " +
                "FROM PUBLIC.FILMS f " +
                "JOIN PUBLIC.RATING r " +
                "ON f.RATING_ID = r.RATING_ID " +
                "WHERE FILM_ID = ?";

        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, new FilmRowMapper(), id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Film> findAll() {
        String sql = "SELECT * " +
                "FROM PUBLIC.FILMS f " +
                "JOIN PUBLIC.RATING r " +
                "ON f.RATING_ID = r.RATING_ID ";

        try {
            return jdbcTemplate.query(sql, new FilmRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<>();
        }
    }

    @Override
    public List<Film> getPopular(Integer limit) {
        String sql = "SELECT * " +
                "FROM FILMS f " +
                "JOIN PUBLIC.RATING r " +
                "ON f.RATING_ID = r.RATING_ID " +
                "LEFT JOIN(SELECT FILM_ID AS fid, COUNT (USER_ID) AS COUNTER " +
                "FROM PUBLIC.LIKES l " +
                "GROUP BY FID) ON F.FILM_ID = fid " +
                "ORDER BY COUNTER DESC " +
                "LIMIT ?";

        try {
            return jdbcTemplate.query(sql, new FilmRowMapper(), limit);
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<>();
        }
    }

    private static class FilmRowMapper implements RowMapper<Film> {
        @Override
        public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
            Mpa mpa = Mpa.builder()
                    .id(rs.getInt("RATING_ID"))
                    .name(rs.getString("RATING_NAME"))
                    .build();
            return Film.builder()
                    .id(rs.getInt("FILM_ID"))
                    .name(rs.getString("NAME"))
                    .description(rs.getString("DESCRIPTION"))
                    .duration(rs.getInt("DURATION"))
                    .releaseDate(LocalDate.parse(rs.getString("RELEASE_DATE")))
                    .mpa(mpa)
                    .build();
        }
    }
}
