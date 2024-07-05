package com.devchristian.quota_management.controller;

import com.devchristian.quota_management.dto.UserQuotaResponseDto;
import com.devchristian.quota_management.service.QuotaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Quota controller.
 */
@RestController
@RequestMapping("/quotas")
public class QuotaController {

    private final QuotaService quotaService;

    @Autowired
    public QuotaController(QuotaService quotaService) {
        this.quotaService = quotaService;
    }

    @PostMapping("/consume/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public String consumeQuota(@PathVariable String userId) {
        quotaService.consumeQuota(userId);
        return "Quota consumed for user " + userId;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<UserQuotaResponseDto> getUsersQuota() {
        return quotaService.getUsersQuota();
    }
}
