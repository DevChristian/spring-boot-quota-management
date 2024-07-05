package com.devchristian.quota_management.service;

import com.devchristian.quota_management.dto.UserQuotaResponseDto;
import com.devchristian.quota_management.entity.User;
import com.devchristian.quota_management.exception.QuotaExceededException;
import com.devchristian.quota_management.repository.DynamicUserRepository;
import com.devchristian.quota_management.util.ServiceUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuotaServiceImpl implements QuotaService {

    private final DynamicUserRepository dynamicUserRepository;

    @Value("${quota.request.limit}")
    private int requestLimit;

    private static final Logger LOGGER = LoggerFactory.getLogger(QuotaServiceImpl.class);


    @Autowired
    public QuotaServiceImpl(DynamicUserRepository dynamicUserRepository) {
        this.dynamicUserRepository = dynamicUserRepository;
    }

    @Override
    public void consumeQuota(String userId) {
        User user = dynamicUserRepository.findById(userId)
                .orElseThrow(() -> ServiceUtils.logAndThrowUserNotFoundException(userId));

        if (user.getQuota() >= requestLimit) {
            throw new QuotaExceededException("User has exceeded their quota of requests");
        }

        user.setLastLoginTimeUtc(LocalDateTime.now(ZoneOffset.UTC));
        user.setQuota(user.getQuota() + 1);
        dynamicUserRepository.save(user);

        LOGGER.info("Quota consumed for user with id: {}", userId);
    }

    @Override
    public List<UserQuotaResponseDto> getUsersQuota() {
        LOGGER.info("Fetching quotas for all users");

        return dynamicUserRepository.findAll().stream()
                .map(this::convertToUserQuotaResponseDto)
                .collect(Collectors.toList());
    }

    @Scheduled(cron = "${scheduling.resetQuotasTime}", zone = "UTC")
    @Transactional
    public void resetQuotas() {
        dynamicUserRepository.resetAllUserQuotas();

        LOGGER.info("Reset quotas for all users");
    }

    private UserQuotaResponseDto convertToUserQuotaResponseDto(User user) {
        return new UserQuotaResponseDto(user.getId(), user.getFirstName(),
                user.getLastName(), user.getQuota(), user.getLastLoginTimeUtc());
    }
}
