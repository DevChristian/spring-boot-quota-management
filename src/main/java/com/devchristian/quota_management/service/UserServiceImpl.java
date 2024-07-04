package com.devchristian.quota_management.service;

import com.devchristian.quota_management.dto.UserRequestDto;
import com.devchristian.quota_management.dto.UserResponseDto;
import com.devchristian.quota_management.entity.User;
import com.devchristian.quota_management.exception.UserNotFoundException;
import com.devchristian.quota_management.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserResponseDto createUser(UserRequestDto userRequestDto) {
        LOGGER.info("Creating user with firstName: {} and lastName: {}",
                userRequestDto.firstName(), userRequestDto.lastName());

        User user = new User();
        updateUserEntity(user, userRequestDto);
        user = userRepository.save(user);

        LOGGER.info("User created with id: {}", user.getId());

        return convertToResponseDto(user);
    }

    @Override
    public UserResponseDto getUser(String userId) {
        LOGGER.info("Fetching user with id: {}", userId);

        return userRepository.findById(userId)
                .map(this::convertToResponseDto)
                .orElseThrow(() -> logAndThrowUserNotFoundException(userId));
    }

    @Override
    public UserResponseDto updateUser(String userId, UserRequestDto updatedUserRequestDto) {
        LOGGER.info("Updating user with id: {}", userId);

        return userRepository.findById(userId).map(user -> {
            updateUserEntity(user, updatedUserRequestDto);
            user = userRepository.save(user);
            LOGGER.info("User updated with id: {}", userId);

            return convertToResponseDto(user);
        }).orElseThrow(() -> logAndThrowUserNotFoundException(userId));
    }

    @Override
    public void deleteUser(String userId) {
        LOGGER.info("Deleting user with id: {}", userId);

        if (userRepository.existsById(userId)) {
            userRepository.deleteById(userId);
            LOGGER.info("User deleted with id: {}", userId);
        } else {
            throw logAndThrowUserNotFoundException(userId);
        }
    }

    private void updateUserEntity(User user, UserRequestDto userRequestDto) {
        user.setFirstName(userRequestDto.firstName());
        user.setLastName(userRequestDto.lastName());
    }

    private UserResponseDto convertToResponseDto(User user) {
        return new UserResponseDto(user.getId(), user.getFirstName(), user.getLastName(), user.getLastLoginTimeUtc());
    }

    private UserNotFoundException logAndThrowUserNotFoundException(String userId) {
        LOGGER.error("User not found with id: {}", userId);
        return new UserNotFoundException("User not found with id: " + userId);
    }
}
