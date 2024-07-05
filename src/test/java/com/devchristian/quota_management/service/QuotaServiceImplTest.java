package com.devchristian.quota_management.service;

import com.devchristian.quota_management.dto.UserQuotaResponseDto;
import com.devchristian.quota_management.entity.User;
import com.devchristian.quota_management.exception.QuotaExceededException;
import com.devchristian.quota_management.exception.UserNotFoundException;
import com.devchristian.quota_management.repository.DynamicUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QuotaServiceImplTest {

    @Mock
    private DynamicUserRepository dynamicUserRepository;

    @InjectMocks
    private QuotaServiceImpl quotaService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId("1");
        user.setFirstName("Chris");
        user.setLastName("Mendez");
        user.setQuota(0);
        user.setLastLoginTimeUtc(LocalDateTime.now(ZoneOffset.UTC));

        ReflectionTestUtils.setField(quotaService, "requestLimit", 5);
    }

    @Test
    void consumeQuota() {
        when(dynamicUserRepository.findById(anyString())).thenReturn(Optional.of(user));

        quotaService.consumeQuota("1");

        verify(dynamicUserRepository, times(1)).save(any(User.class));
    }

    @Test
    void consumeQuota_UserNotFound() {
        when(dynamicUserRepository.findById(anyString())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> quotaService.consumeQuota("1"));
    }

    @Test
    void consumeQuota_QuotaExceeded() {
        user.setQuota(10);
        when(dynamicUserRepository.findById(anyString())).thenReturn(Optional.of(user));

        assertThrows(QuotaExceededException.class, () -> quotaService.consumeQuota("1"));
    }

    @Test
    void getUsersQuota() {
        when(dynamicUserRepository.findAll()).thenReturn(Collections.singletonList(user));

        List<UserQuotaResponseDto> userQuotaResponseDtoList = quotaService.getUsersQuota();

        assertEquals(1, userQuotaResponseDtoList.size());
        assertEquals("1", userQuotaResponseDtoList.get(0).id());
        verify(dynamicUserRepository, times(1)).findAll();
    }

    @Test
    void resetQuotas() {
        doNothing().when(dynamicUserRepository).resetAllUserQuotas();

        quotaService.resetQuotas();

        verify(dynamicUserRepository, times(1)).resetAllUserQuotas();
    }
}