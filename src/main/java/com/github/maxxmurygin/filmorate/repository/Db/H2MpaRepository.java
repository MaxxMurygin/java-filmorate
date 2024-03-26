package com.github.maxxmurygin.filmorate.repository.Db;

import com.github.maxxmurygin.filmorate.model.Mpa;
import com.github.maxxmurygin.filmorate.repository.MpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
@Primary
@Slf4j
public class H2MpaRepository implements MpaRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public Mpa findById(int mpaId) {
        Map<String, Object> params = new HashMap<>();
        String sql = "SELECT RATING_ID, RATING_NAME " +
                "FROM PUBLIC.RATING " +
                "WHERE RATING_ID = :mpaId";

        params.put("mpaId", mpaId);
        try {
            return jdbcTemplate.queryForObject(sql, params, new MpaRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<Mpa> findAll() {
        String sql = "SELECT RATING_ID, RATING_NAME " +
                "FROM PUBLIC.RATING";

        try {
            return jdbcTemplate.query(sql, new MpaRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<>();
        }
    }

    private static class MpaRowMapper implements RowMapper<Mpa> {
        @Override
        public Mpa mapRow(ResultSet rs, int rowNum) throws SQLException {
            return Mpa.builder()
                    .id(rs.getInt("RATING_ID"))
                    .name(rs.getString("RATING_NAME"))
                    .build();
        }
    }
}
