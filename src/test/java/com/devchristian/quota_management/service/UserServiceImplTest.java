package com.devchristian.quota_management.service;

import com.devchristian.quota_management.dto.UserRequestDto;
import com.devchristian.quota_management.dto.UserResponseDto;
import com.devchristian.quota_management.entity.User;
import com.devchristian.quota_management.exception.UserNotFoundException;
import com.devchristian.quota_management.repository.DynamicUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private DynamicUserRepository dynamicUserRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    private UserRequestDto userRequestDto;
    private UserResponseDto userResponseDto;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId("1");
        user.setFirstName("Chris");
        user.setLastName("Mendez");

        userRequestDto = new UserRequestDto("Chris", "Mendez");
        userResponseDto = new UserResponseDto("1", "Chris",
                "Mendez", 0, null);
    }

    @Test
    void createUser() {
        when(dynamicUserRepository.save(any(User.class))).thenReturn(user);

        UserResponseDto createdUser = userService.createUser(userRequestDto);

        assertEquals(userResponseDto, createdUser);
        verify(dynamicUserRepository, times(1)).save(any(User.class));
    }

    @Test
    void getUser() {
        when(dynamicUserRepository.findById(anyString())).thenReturn(Optional.of(user));

        UserResponseDto foundUser = userService.getUser("1");

        assertEquals(userResponseDto, foundUser);
        verify(dynamicUserRepository, times(1)).findById("1");
    }

    @Test
    void getUser_UserNotFound() {
        when(dynamicUserRepository.findById(anyString())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUser("1"));
        verify(dynamicUserRepository, times(1)).findById("1");
    }

    @Test
    void updateUser() {
        when(dynamicUserRepository.findById(anyString())).thenReturn(Optional.of(user));
        when(dynamicUserRepository.save(any(User.class))).thenReturn(user);

        UserResponseDto updatedUser = userService.updateUser("1", userRequestDto);

        assertEquals(userResponseDto, updatedUser);
        verify(dynamicUserRepository, times(1)).findById("1");
        verify(dynamicUserRepository, times(1)).save(user);
    }

    @Test
    void updateUser_UserNotFound() {
        when(dynamicUserRepository.findById(anyString())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.updateUser("1", userRequestDto));
        verify(dynamicUserRepository, times(1)).findById("1");
        verify(dynamicUserRepository, times(0)).save(any(User.class));
    }

    @Test
    void deleteUser() {
        when(dynamicUserRepository.existsById(anyString())).thenReturn(true);
        doNothing().when(dynamicUserRepository).deleteById(anyString());

        userService.deleteUser("1");

        verify(dynamicUserRepository, times(1)).existsById("1");
        verify(dynamicUserRepository, times(1)).deleteById("1");
    }

    @Test
    void deleteUser_UserNotFound() {
        when(dynamicUserRepository.existsById(anyString())).thenReturn(false);

        assertThrows(UserNotFoundException.class, () -> userService.deleteUser("1"));
        verify(dynamicUserRepository, times(1)).existsById("1");
        verify(dynamicUserRepository, times(0)).deleteById("1");
    }
}