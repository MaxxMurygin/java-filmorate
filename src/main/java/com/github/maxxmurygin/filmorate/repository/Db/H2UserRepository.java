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
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Primary
@Slf4j
public class H2UserRepository implements UserRepository {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public User create(User user) {
        try {
            final String sql = "INSERT INTO PUBLIC.USERS (EMAIL, LOGIN, NAME, BIRTHDAY) VALUES(?, ?, ?, ?)";
            KeyHolder keyHolder = new GeneratedKeyHolder();

            log.debug("H2: User {} start create into DB", user.getLogin());
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection
                        .prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, user.getEmail());
                ps.setString(2, user.getLogin());
                ps.setString(3, user.getName());
                ps.setObject(4, user.getBirthday());
                return ps;
            }, keyHolder);
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
        String sql = "SELECT USER_ID, EMAIL, LOGIN, NAME, BIRTHDAY " +
                "FROM PUBLIC.USERS " +
                "WHERE LOGIN = ?";

        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, new UserRowMapper(),login));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> findByEmail(String email) {
        String sql = "SELECT USER_ID, EMAIL, LOGIN, NAME, BIRTHDAY " +
                "FROM PUBLIC.USERS " +
                "WHERE EMAIL = ?";

        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, new UserRowMapper(),email));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> findById(Integer id) {
        String sql = "SELECT USER_ID, EMAIL, LOGIN, NAME, BIRTHDAY " +
                "FROM PUBLIC.USERS " +
                "WHERE USER_ID = ?";

        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, new UserRowMapper(), id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public User update(User user) {
        String sql = "UPDATE PUBLIC.USERS SET EMAIL = ?, LOGIN = ?, NAME = ?, BIRTHDAY = ? " +
                "WHERE USER_ID = ?";

        try {
            jdbcTemplate.update(sql, user.getEmail(), user.getLogin(),
                    user.getName(), user.getBirthday(), user.getId());
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
