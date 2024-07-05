package com.devchristian.quota_management.repository;

import com.devchristian.quota_management.entity.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * User repository.
 */
public interface UserRepository {
    /**
     * Save a user.
     *
     * @param user the user
     * @return the saved user
     */
    User save(User user);
    /**
     * Find a user by ID.
     *
     * @param userId the user ID
     * @return the user
     */
    Optional<User> findById(String userId);
    /**
     * Delete a user by ID.
     *
     * @param userId the user ID
     */
    void deleteById(String userId);
    /**
     * Check if a user exists by ID.
     *
     * @param userId the user ID
     * @return true if the user exists, false otherwise
     */
    boolean existsById(String userId);
    /**
     * Find all users.
     *
     * @return the list of users
     */
    List<User> findAll();
    /**
     * Reset all user quotas.
     */
    @Modifying
    @Query("UPDATE User u SET u.quota = 0")
    void resetAllUserQuotas();
}
