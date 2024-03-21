package com.github.maxxmurygin.filmorate.repository.Db;

import com.github.maxxmurygin.filmorate.exeptions.LikeAlreadyExistException;
import com.github.maxxmurygin.filmorate.model.Friends;
import com.github.maxxmurygin.filmorate.model.User;
import com.github.maxxmurygin.filmorate.repository.FriendsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
@Primary
@Slf4j
public class H2FriendsRepository implements FriendsRepository {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void addFriend(Integer followingUserId, Integer followedUserid) {
        String sql = "INSERT INTO PUBLIC.FOLLOWS (FOLLOWING_USER_ID, FOLLOWED_USER_ID, CREATED_AT) " +
                "VALUES (?, ?, ?)";

        log.debug("H2: Добавляем пользователю {} друга {}", followingUserId, followedUserid);
        try {
            jdbcTemplate.update(sql, followingUserId, followedUserid, LocalDateTime.now());
        } catch (DuplicateKeyException e) {
            log.debug("Пользователь ID = {} уже дружит с  ID = {}", followingUserId, followedUserid);
//            throw new LikeAlreadyExistException(String.format("Пользователь ID = %d уже дружит с  ID = %d",
//                    followingUserId, followedUserid));
        }
        log.debug("H2: Добавлен пользователю {} друг {}", followingUserId, followedUserid);
    }

    @Override
    public void removeFriend(Integer followingUserId, Integer followedUserid) {
        String sql = "DELETE FROM PUBLIC.FOLLOWS  " +
                "WHERE FOLLOWING_USER_ID = ? AND  FOLLOWED_USER_ID = ?";

        jdbcTemplate.update(sql, followingUserId, followedUserid);
    }

    @Override
    public List<Integer> findFriends(Integer userId) {
        String sql = "SELECT * " +
                "FROM PUBLIC.FOLLOWS " +
                "WHERE FOLLOWING_USER_ID = ?";

        try {
            return jdbcTemplate.query(sql, new FriendsRowMapper(), userId)
                    .stream()
                    .map(Friends::getFollowedUserid)
                    .collect(Collectors.toList());
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<>();
        }
    }

    @Override
    public List<Integer> findCommonFriends(Integer userId, Integer otherId) {
        String sql = "SELECT  * " +
                "FROM PUBLIC.FOLLOWS " +
                "WHERE FOLLOWING_USER_ID = ? AND FOLLOWED_USER_ID IN (" +
                "SELECT FOLLOWED_USER_ID " +
                "FROM PUBLIC.FOLLOWS " +
                "WHERE FOLLOWING_USER_ID = ?)";

        try {
            return jdbcTemplate.query(sql, new FriendsRowMapper(), userId, otherId)
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
