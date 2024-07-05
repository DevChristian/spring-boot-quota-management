package com.devchristian.quota_management.service;

import com.devchristian.quota_management.dto.UserQuotaResponseDto;

import java.util.List;

/**
 * Quota service.
 */
public interface QuotaService {
    /**
     * Consume quota for a user.
     *
     * @param userId the user ID
     */
    void consumeQuota(String userId);
    /**
     * Get quota for all users.
     *
     * @return the list of user quota response DTOs
     */
    List<UserQuotaResponseDto> getUsersQuota();
}
