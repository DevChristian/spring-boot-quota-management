package com.devchristian.quota_management.service;

import com.devchristian.quota_management.dto.UserRequestDto;
import com.devchristian.quota_management.dto.UserResponseDto;

import java.util.Optional;

public interface UserService {
    UserResponseDto createUser(UserRequestDto userRequestDto);
    UserResponseDto getUser(String userId);
    UserResponseDto updateUser(String userId, UserRequestDto updatedUserRequestDto);
    void deleteUser(String userId);
}
