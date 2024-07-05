package com.devchristian.quota_management.repository;

import com.devchristian.quota_management.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DynamicUserRepositoryTest {

    @Mock
    private MySqlUserRepository mySqlUserRepository;

    @Mock
    private ElasticUserRepository elasticUserRepository;

    @InjectMocks
    private DynamicUserRepository dynamicUserRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(dynamicUserRepository, "mySqlTimeStart", 0);
        ReflectionTestUtils.setField(dynamicUserRepository, "mySqlTimeEnd", 0);
    }

    @Test
    void save() {
        User user = new User();
        when(elasticUserRepository.save(user)).thenReturn(user);

        User savedUser = dynamicUserRepository.save(user);

        assertEquals(user, savedUser);
        verify((UserRepository) mySqlUserRepository, times(0)).save(user);
        verify(elasticUserRepository, times(1)).save(user);
    }

    @Test
    void findById() {
        User user = new User();
        when(elasticUserRepository.findById("1")).thenReturn(Optional.of(user));

        Optional<User> foundUser = dynamicUserRepository.findById("1");

        assertTrue(foundUser.isPresent());
        assertEquals(user, foundUser.get());
        verify((UserRepository) mySqlUserRepository, times(0)).findById("1");
        verify(elasticUserRepository, times(1)).findById("1");
    }

    @Test
    void deleteById() {
        dynamicUserRepository.deleteById("1");

        verify((UserRepository) mySqlUserRepository, times(0)).deleteById("1");
        verify(elasticUserRepository, times(1)).deleteById("1");
    }

    @Test
    void existsById() {
        when((elasticUserRepository).existsById("1")).thenReturn(true);

        boolean exists = dynamicUserRepository.existsById("1");

        assertTrue(exists);
        verify((UserRepository) mySqlUserRepository, times(0)).existsById("1");
        verify(elasticUserRepository, times(1)).existsById("1");
    }

    @Test
    void findAll() {
        dynamicUserRepository.findAll();

        verify(mySqlUserRepository, times(0)).findAll();
        verify(elasticUserRepository, times(1)).findAll();
    }

    @Test
    void resetAllUserQuotas() {
        dynamicUserRepository.resetAllUserQuotas();

        verify(mySqlUserRepository, times(0)).resetAllUserQuotas();
        verify(elasticUserRepository, times(1)).resetAllUserQuotas();
    }
}