package com.github.maxxmurygin.filmorate.repository.Db;

import com.github.maxxmurygin.filmorate.exeptions.LikeAlreadyExistException;
import com.github.maxxmurygin.filmorate.repository.LikesRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
@Primary
@Slf4j
public class H2LikesRepository implements LikesRepository {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void addLike(Integer filmId, Integer userId) {
        String sql = "INSERT INTO PUBLIC.LIKES (USER_ID, FILM_ID, CREATED_AT, IS_LIKE) " +
                "VALUES (?, ?, ?, ?)";

        try {
            jdbcTemplate.update(sql, userId, filmId, LocalDateTime.now(), true);
        } catch (DuplicateKeyException e) {
            log.debug("Лайк от пользователя ID = {}  фильму ID = {} уже существует", userId, filmId);
            throw new LikeAlreadyExistException(String.format("Лайк от пользователя ID = %d  " +
                    "фильму ID = %d уже существует", userId, filmId));
        }
    }

    @Override
    public void removeLike(Integer filmId, Integer userId) {
        String sql = "DELETE FROM PUBLIC.LIKES  " +
                "WHERE FILM_ID = ? AND USER_ID = ?";

        jdbcTemplate.update(sql, userId, filmId);
    }
}
