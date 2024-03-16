package com.github.maxxmurygin.filmorate.repository;

import java.util.List;

public interface FriendsRepository {
    void addFriend(Integer followingUserId, Integer followedUserid);
    void removeFriend(Integer followingUserId, Integer followedUserid);
    void confirmFriendship(Integer followingUserId, Integer followedUserid);
    List<Integer> findFriends(Integer userId);
    List<Integer> findCommonFriends(Integer userId, Integer otherId);
}
