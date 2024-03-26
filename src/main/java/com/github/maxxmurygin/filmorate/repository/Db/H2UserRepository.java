package com.github.maxxmurygin.filmorate.repository.Db;

import com.github.maxxmurygin.filmorate.exeptions.UserValidationException;
import com.github.maxxmurygin.filmorate.model.User;
import com.github.maxxmurygin.filmorate.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.DuplicateKeyException;
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
public class H2UserRepository implements UserRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public User create(User user) {
        Map<String, Object> params = new HashMap<>();
        KeyHolder keyHolder = new GeneratedKeyHolder();
        final String sql = "INSERT INTO PUBLIC.USERS (EMAIL, LOGIN, NAME, BIRTHDAY) " +
                "VALUES(:email, :login, :name, :birthday)";

        params.put("email", user.getEmail());
        params.put("login", user.getLogin());
        params.put("name", user.getName());
        params.put("birthday", user.getBirthday());
        SqlParameterSource paramSource = new MapSqlParameterSource(params);
        try {
            log.debug("H2: User {} start create into DB", user.getLogin());
            jdbcTemplate.update(sql, paramSource, keyHolder);
            user.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());
            log.debug("H2: User {} end create into DB", user.getLogin());
            return user;
        } catch (DuplicateKeyException e) {
            String errorMessage = Objects.requireNonNull(e.getMessage());

            if (errorMessage.contains("LOGIN_UNIQUE_INDEX")) {
                throw new UserValidationException(String.format("Логин %s уже есть в БД", user.getLogin()));
            }
            if (errorMessage.contains("EMAIL_UNIQUE_INDEX")) {
                throw new UserValidationException(String.format("Email %s уже есть в БД", user.getEmail()));
            }
            throw new UserValidationException(errorMessage);
        }
    }

    @Override
    public Optional<User> findByLogin(String login) {
        Map<String, Object> params = new HashMap<>();
        String sql = "SELECT USER_ID, EMAIL, LOGIN, NAME, BIRTHDAY " +
                "FROM PUBLIC.USERS " +
                "WHERE LOGIN = :login";

        params.put("login", login);
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, params, new UserRowMapper()));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> findByEmail(String email) {
        Map<String, Object> params = new HashMap<>();
        String sql = "SELECT USER_ID, EMAIL, LOGIN, NAME, BIRTHDAY " +
                "FROM PUBLIC.USERS " +
                "WHERE EMAIL = :email";

        params.put("email", email);
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, params, new UserRowMapper()));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> findById(Integer userId) {
        Map<String, Object> params = new HashMap<>();
        String sql = "SELECT USER_ID, EMAIL, LOGIN, NAME, BIRTHDAY " +
                "FROM PUBLIC.USERS " +
                "WHERE USER_ID = :userId";

        params.put("userId", userId);
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, params, new UserRowMapper()));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public User update(User user) {
        Map<String, Object> params = new HashMap<>();
        String sql = "UPDATE PUBLIC.USERS SET EMAIL = :email, LOGIN = :login, NAME = :name, BIRTHDAY = :birthday " +
                "WHERE USER_ID = :userId";
        params.put("email", user.getEmail());
        params.put("login", user.getLogin());
        params.put("name", user.getName());
        params.put("birthday", user.getBirthday());
        params.put("userId", user.getId());
        try {
            jdbcTemplate.update(sql, params);
        } catch (DuplicateKeyException e) {
            String errorMessage = Objects.requireNonNull(e.getMessage());

            if (errorMessage.contains("LOGIN_UNIQUE_INDEX")) {
                throw new UserValidationException(String.format("Логин %s уже есть в БД", user.getLogin()));
            }
            if (errorMessage.contains("EMAIL_UNIQUE_INDEX")) {
                throw new UserValidationException(String.format("Email %s уже есть в БД", user.getEmail()));
            }
            throw new UserValidationException(errorMessage);
        }
        return user;
    }

    @Override
    public Collection<User> findAll() {
        String sql = "SELECT USER_ID, EMAIL, LOGIN, NAME, BIRTHDAY FROM PUBLIC.USERS";

        try {
            return jdbcTemplate.query(sql, new UserRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    private static class UserRowMapper implements RowMapper<User> {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            return User.builder()
                    .id(rs.getInt("USER_ID"))
                    .email(rs.getString("EMAIL"))
                    .login(rs.getString("LOGIN"))
                    .name(rs.getString("NAME"))
                    .birthday(LocalDate.parse(rs.getString("BIRTHDAY")))
                    .build();
        }
    }
}
