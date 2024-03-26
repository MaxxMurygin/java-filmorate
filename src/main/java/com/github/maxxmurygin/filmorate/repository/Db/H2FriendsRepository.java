package com.github.maxxmurygin.filmorate.repository.Db;

import com.github.maxxmurygin.filmorate.model.Friends;
import com.github.maxxmurygin.filmorate.repository.FriendsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
@Primary
@Slf4j
public class H2FriendsRepository implements FriendsRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public void addFriend(Integer followingUserId, Integer followedUserid) {
        Map<String, Object> params = new HashMap<>();
        String sql = "INSERT INTO PUBLIC.FOLLOWS (FOLLOWING_USER_ID, FOLLOWED_USER_ID, CREATED_AT) " +
                "VALUES (:followingUserId, :followedUserId, :creationDate)";

        log.debug("H2: Добавляем пользователю {} друга {}", followingUserId, followedUserid);
        params.put("followingUserId", followingUserId);
        params.put("followedUserId", followedUserid);
        params.put("creationDate", LocalDateTime.now());
        try {
            jdbcTemplate.update(sql, params);
        } catch (DuplicateKeyException e) {
            log.debug("Пользователь ID = {} уже дружит с  ID = {}", followingUserId, followedUserid);
        }
        log.debug("H2: Добавлен пользователю {} друг {}", followingUserId, followedUserid);
    }

    @Override
    public void removeFriend(Integer followingUserId, Integer followedUserid) {
        Map<String, Object> params = new HashMap<>();
        String sql = "DELETE FROM PUBLIC.FOLLOWS  " +
                "WHERE FOLLOWING_USER_ID = :followingUserId AND  FOLLOWED_USER_ID = :followedUserId";

        params.put("followingUserId", followingUserId);
        params.put("followedUserId", followedUserid);
        jdbcTemplate.update(sql, params);
    }

    @Override
    public List<Integer> findFriends(Integer userId) {
        Map<String, Object> params = new HashMap<>();
        String sql = "SELECT * " +
                "FROM PUBLIC.FOLLOWS " +
                "WHERE FOLLOWING_USER_ID = :userId";

        params.put("userId", userId);
        try {
            return jdbcTemplate.query(sql, params, new FriendsRowMapper())
                    .stream()
                    .map(Friends::getFollowedUserid)
                    .collect(Collectors.toList());
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<>();
        }
    }

    @Override
    public List<Integer> findCommonFriends(Integer userId, Integer otherId) {
        Map<String, Object> params = new HashMap<>();
        String sql = "SELECT  * " +
                "FROM PUBLIC.FOLLOWS " +
                "WHERE FOLLOWING_USER_ID = :userId AND FOLLOWED_USER_ID IN (" +
                "SELECT FOLLOWED_USER_ID " +
                "FROM PUBLIC.FOLLOWS " +
                "WHERE FOLLOWING_USER_ID = :otherId)";

        params.put("userId", userId);
        params.put("otherId", otherId);
        try {
            return jdbcTemplate.query(sql, params, new FriendsRowMapper())
                    .stream()
                    .map(Friends::getFollowedUserid)
                    .collect(Collectors.toList());
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<>();
        }
    }

    private static class FriendsRowMapper implements RowMapper<Friends> {
        @Override
        public Friends mapRow(ResultSet rs, int rowNum) throws SQLException {
            return Friends.builder()
                    .followingUserId(rs.getInt("FOLLOWING_USER_ID"))
                    .followedUserid(rs.getInt("FOLLOWED_USER_ID"))
                    .build();
        }
    }
}
