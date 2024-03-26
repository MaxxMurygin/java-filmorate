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
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.*;

@Repository
@RequiredArgsConstructor
@Primary
@Slf4j
public class H2FilmRepository implements FilmRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public Film create(Film film) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        Map<String, Object> params = new HashMap<>();
        final String sql = "INSERT INTO PUBLIC.FILMS (NAME, DESCRIPTION, RELEASE_DATE, DURATION, RATING_ID) " +
                "VALUES(:name, :description, :releaseDate, :duration, :mpaId)";

        params.put("name", film.getName());
        params.put("description", film.getDescription());
        params.put("releaseDate", film.getReleaseDate());
        params.put("duration", film.getDuration());
        params.put("mpaId", film.getMpa().getId());
        SqlParameterSource paramSource = new MapSqlParameterSource(params);

        try {
            log.debug("H2: Film {} start create into DB", film.getName());
            jdbcTemplate.update(sql, paramSource, keyHolder);
            film.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());
            log.debug("H2: Film {} end create into DB", film.getName());
            return film;
        } catch (DataAccessException e) {
            throw new FilmValidationException(e.getMessage());
        }
    }

    @Override
    public Film update(Film film) {
        Map<String, Object> params = new HashMap<>();
        String sql = "UPDATE PUBLIC.FILMS SET NAME = :name, DESCRIPTION = :description, " +
                "RELEASE_DATE = :releaseDate, DURATION = :duration, RATING_ID = :mpaId " +
                "WHERE FILM_ID = :filmId";

        log.debug("H2: Film {} start update into DB", film.getName());
        params.put("name", film.getName());
        params.put("description", film.getDescription());
        params.put("releaseDate", film.getReleaseDate());
        params.put("duration", film.getDuration());
        params.put("mpaId", film.getMpa().getId());
        params.put("filmId", film.getId());
        try {
            jdbcTemplate.update(sql, params);
        } catch (DataAccessException e) {
            throw new FilmValidationException(e.getMessage());
        }
        log.debug("H2: Film {} end update into DB", film.getName());
        return film;
    }

    @Override
    public Optional<Film> findById(Integer filmId) {
        Map<String, Object> params = new HashMap<>();
        String sql = "SELECT * " +
                "FROM PUBLIC.FILMS f " +
                "JOIN PUBLIC.RATING r " +
                "ON f.RATING_ID = r.RATING_ID " +
                "WHERE FILM_ID = :filmId";

        params.put("filmId", filmId);
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, params, new FilmRowMapper()));
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
        Map<String, Object> params = new HashMap<>();
        String sql = "SELECT * " +
                "FROM FILMS f " +
                "JOIN PUBLIC.RATING r " +
                "ON f.RATING_ID = r.RATING_ID " +
                "LEFT JOIN(SELECT FILM_ID AS fid, COUNT (USER_ID) AS COUNTER " +
                "FROM PUBLIC.LIKES l " +
                "GROUP BY FID) ON F.FILM_ID = fid " +
                "ORDER BY COUNTER DESC " +
                "LIMIT :limit";

        params.put("limit", limit);
        try {
            return jdbcTemplate.query(sql, params, new FilmRowMapper());
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
