package com.github.maxxmurygin.filmorate.repository.InMemory;

import com.github.maxxmurygin.filmorate.repository.FriendsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@Slf4j
public class InMemoryFriendsRepository implements FriendsRepository {
    private final HashMap<Integer, Set<Integer>> friends = new HashMap<>();

    @Override
    public void addFriend(Integer followingUserId, Integer followedUserid) {
        if (!friends.containsKey(followingUserId)) {
            friends.put(followingUserId, new HashSet<>());
        }
        if (!friends.containsKey(followedUserid)) {
            friends.put(followedUserid, new HashSet<>());
        }
        friends.get(followingUserId).add(followedUserid);
        friends.get(followedUserid).add(followingUserId);
    }

    @Override
    public void removeFriend(Integer followingUserId, Integer followedUserid) {
        if (!friends.containsKey(followingUserId)) {
            friends.put(followingUserId, new HashSet<>());
        }
        friends.get(followingUserId).remove(followedUserid);
    }

    @Override
    public List<Integer> findFriends(Integer userId) {
        Set<Integer> userFriends = friends.get(userId);

        if (userFriends == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(userFriends);
    }

    @Override
    public List<Integer> findCommonFriends(Integer userId, Integer otherId) {
        Set<Integer> userFriends = friends.get(userId);
        if (userFriends == null) {
            userFriends = new HashSet<>();
        }

        Set<Integer> otherFriends = friends.get(otherId);
        if (otherFriends == null) {
            otherFriends = new HashSet<>();
        }
        ArrayList<Integer> commonFriends = new ArrayList<>(userFriends);
        commonFriends.retainAll(otherFriends);
        return commonFriends;
    }
}
