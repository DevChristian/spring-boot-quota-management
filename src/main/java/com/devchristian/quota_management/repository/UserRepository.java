package com.devchristian.quota_management.repository;

import com.devchristian.quota_management.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
}
