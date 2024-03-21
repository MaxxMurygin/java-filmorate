package com.github.maxxmurygin.filmorate.repository.Db;

import com.github.maxxmurygin.filmorate.model.User;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDate;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class H2UserRepositoryTest {
    private final JdbcTemplate jdbcTemplate;
    private H2UserRepository userRepository;
    private User user;

    @BeforeEach
    void init() {
        userRepository = new H2UserRepository(jdbcTemplate);
        user = User.builder()
                .login("Vincent")
                .name("Vincent Willem van Gogh")
                .email("vinc@vincent.net")
                .birthday(LocalDate.of(1853, 3, 30))
                .build();
    }
    @Test
    void create() {
        userRepository.create(user);
        User stored = userRepository.findById(1).orElse(null);

        assertNotNull(stored);
        assertEquals(user, stored);

    }

    @Test
    void findByLogin() {
        userRepository.create(user);
        User stored = userRepository.findByLogin("Vincent").orElse(null);

        assertNotNull(stored);
        assertEquals(user, stored);
    }

    @Test
    void findByEmail() {
        userRepository.create(user);
        User stored = userRepository.findByEmail("vinc@vincent.net").orElse(null);

        assertNotNull(stored);
        assertEquals(user, stored);
    }

    @Test
    void update() {
        userRepository.create(user);
        user.setName("New Vincent");
        user.setEmail("vinc@vincent.net");
        userRepository.update(user);
        User stored = userRepository.findByLogin("Vincent").orElse(null);

        assertNotNull(stored);
        assertEquals(stored.getName(), "New Vincent");
        assertEquals(stored.getEmail(), "vinc@vincent.net");
    }

    @Test
    void findAll() {
        userRepository.create(user);
        user.setLogin("Vincent1");
        user.setEmail("vinc1@vincent.net");
        userRepository.create(user);
        Collection<User> allUsers = userRepository.findAll();
        assertEquals(allUsers.size(), 2);
    }
}