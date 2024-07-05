package com.devchristian.quota_management.dto;

import java.time.LocalDateTime;

public record UserQuotaResponseDto(String id, String firstName,
                                   String lastName, int quota, LocalDateTime lastLoginTimeUtc){

}
