package com.devchristian.quota_management.dto;

import java.time.LocalDateTime;

public record UserResponseDto(String id, String firstName, String lastName, LocalDateTime lastLoginTimeUtc) {
}
