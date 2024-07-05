package com.devchristian.quota_management.repository;

import com.devchristian.quota_management.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ElasticUserRepositoryTest {

    private ElasticUserRepository elasticUserRepository;

    @BeforeEach
    void setUp() {
        elasticUserRepository = new ElasticUserRepository();
    }

    @Test
    void save() {
        User user = new User();
        user.setFirstName("Chris");
        user.setLastName("Mendez");
        user.setQuota(0);

        User savedUser = elasticUserRepository.save(user);

        assertNotNull(savedUser.getId());
        assertEquals(user.getFirstName(), savedUser.getFirstName());
        assertEquals(user.getLastName(), savedUser.getLastName());
        assertEquals(user.getQuota(), savedUser.getQuota());
    }

    @Test
    void findById() {
        User user = new User();
        user.setFirstName("Chris");
        user.setLastName("Mendez");
        user.setQuota(0);

        User savedUser = elasticUserRepository.save(user);

        Optional<User> foundUser = elasticUserRepository.findById(savedUser.getId());

        assertTrue(foundUser.isPresent());
        assertEquals(savedUser, foundUser.get());
    }

    @Test
    void deleteById() {
        User user = new User();
        user.setFirstName("Chris");
        user.setLastName("Mendez");
        user.setQuota(0);

        User savedUser = elasticUserRepository.save(user);

        elasticUserRepository.deleteById(savedUser.getId());

        Optional<User> foundUser = elasticUserRepository.findById(savedUser.getId());

        assertFalse(foundUser.isPresent());
    }

    @Test
    void existsById() {
        User user = new User();
        user.setFirstName("Chris");
        user.setLastName("Mendez");
        user.setQuota(0);

        User savedUser = elasticUserRepository.save(user);

        assertTrue(elasticUserRepository.existsById(savedUser.getId()));
        assertFalse(elasticUserRepository.existsById("non-existing-id"));
    }

    @Test
    void findAll() {
        User user1 = new User();
        user1.setFirstName("Chris");
        user1.setLastName("Mendez");
        user1.setQuota(0);

        User user2 = new User();
        user2.setFirstName("John");
        user2.setLastName("Doe");
        user2.setQuota(0);

        elasticUserRepository.save(user1);
        elasticUserRepository.save(user2);

        List<User> users = elasticUserRepository.findAll();

        assertEquals(2, users.size());
    }

    @Test
    void resetAllUserQuotas() {
        User user1 = new User();
        user1.setFirstName("Chris");
        user1.setLastName("Mendez");
        user1.setQuota(5);

        User user2 = new User();
        user2.setFirstName("John");
        user2.setLastName("Doe");
        user2.setQuota(10);

        elasticUserRepository.save(user1);
        elasticUserRepository.save(user2);

        elasticUserRepository.resetAllUserQuotas();

        List<User> users = elasticUserRepository.findAll();

        for (User user : users) {
            assertEquals(0, user.getQuota());
        }
    }
}