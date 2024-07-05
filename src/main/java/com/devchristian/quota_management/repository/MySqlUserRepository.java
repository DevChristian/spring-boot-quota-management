package com.devchristian.quota_management.repository;

import com.devchristian.quota_management.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * MySQL's user repository.
 */
public interface MySqlUserRepository extends JpaRepository<User, String>, UserRepository {
}
