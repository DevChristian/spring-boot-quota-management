package com.devchristian.quota_management.service;

import com.devchristian.quota_management.dto.UserRequestDto;
import com.devchristian.quota_management.dto.UserResponseDto;

/**
 * User service.
 */
public interface UserService {
    /**
     * Create a new user.
     *
     * @param userRequestDto the user request DTO
     * @return the user response DTO
     */
    UserResponseDto createUser(UserRequestDto userRequestDto);
    /**
     * Get a user by ID.
     *
     * @param userId the user ID
     * @return the user response DTO
     */
    UserResponseDto getUser(String userId);
    /**
     * Update a user.
     *
     * @param userId the user ID
     * @param updatedUserRequestDto the updated user request DTO
     * @return the user response DTO
     */
    UserResponseDto updateUser(String userId, UserRequestDto updatedUserRequestDto);
    /**
     * Delete a user.
     *
     * @param userId the user ID
     */
    void deleteUser(String userId);
}
