package com.devchristian.quota_management.repository;

import com.devchristian.quota_management.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class ElasticUserRepository implements UserRepository {

    private final Map<String, User> userStorage = new HashMap<>();
    private static final Logger LOGGER = LoggerFactory.getLogger(ElasticUserRepository.class);
    private long userIdCounter = 0;

    @Override
    public User save(User user) {
        if (user.getId() == null) {
            user.setId(String.valueOf(++userIdCounter));
        }
        userStorage.put(user.getId(), user);
        LOGGER.info("Simulated ElasticSearch: Saving user {} {}", user.getFirstName(), user.getLastName());
        return user;
    }

    @Override
    public Optional<User> findById(String userId) {
        LOGGER.info("Simulated Elastic: Fetching user with id {}", userId);
        return Optional.ofNullable(userStorage.get(userId));
    }

    @Override
    public void deleteById(String userId) {
        LOGGER.info("Simulated Elastic: Deleting user with id {}", userId);
        userStorage.remove(userId);
    }

    @Override
    public boolean existsById(String userId) {
        LOGGER.info("Simulated Elastic: Checking existence of user with id {}", userId);
        return userStorage.containsKey(userId);
    }

    @Override
    public List<User> findAll() {
        LOGGER.info("Simulated Elastic: Fetching all users");
        return new ArrayList<>(userStorage.values());
    }

    @Override
    public void resetAllUserQuotas() {
        LOGGER.info("Simulated Elastic: Resetting all user quotas");
        userStorage.values().forEach(user -> user.setQuota(0));
    }
}
