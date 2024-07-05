package com.devchristian.quota_management.service;

import com.devchristian.quota_management.dto.UserRequestDto;
import com.devchristian.quota_management.dto.UserResponseDto;
import com.devchristian.quota_management.entity.User;
import com.devchristian.quota_management.repository.DynamicUserRepository;
import com.devchristian.quota_management.util.ServiceUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    private final DynamicUserRepository dynamicUserRepository;

    @Autowired
    public UserServiceImpl(DynamicUserRepository dynamicUserRepository) {
        this.dynamicUserRepository = dynamicUserRepository;
    }

    @Override
    public UserResponseDto createUser(UserRequestDto userRequestDto) {
        LOGGER.info("Creating user with firstName: {} and lastName: {}",
                userRequestDto.firstName(), userRequestDto.lastName());

        User user = new User();
        updateUserEntity(user, userRequestDto);

        user = dynamicUserRepository.save(user);

        LOGGER.info("User created with id: {}", user.getId());

        return convertToUserResponseDto(user);
    }

    @Override
    public UserResponseDto getUser(String userId) {
        LOGGER.info("Fetching user with id: {}", userId);

        return dynamicUserRepository.findById(userId)
                .map(this::convertToUserResponseDto)
                .orElseThrow(() -> ServiceUtils.logAndThrowUserNotFoundException(userId));
    }

    @Override
    public UserResponseDto updateUser(String userId, UserRequestDto updatedUserRequestDto) {
        LOGGER.info("Updating user with id: {}", userId);

        return dynamicUserRepository.findById(userId).map(user -> {
            updateUserEntity(user, updatedUserRequestDto);
            user = dynamicUserRepository.save(user);
            LOGGER.info("User updated with id: {}", userId);

            return convertToUserResponseDto(user);
        }).orElseThrow(() -> ServiceUtils.logAndThrowUserNotFoundException(userId));
    }

    @Override
    public void deleteUser(String userId) {
        LOGGER.info("Deleting user with id: {}", userId);

        if (dynamicUserRepository.existsById(userId)) {
            dynamicUserRepository.deleteById(userId);
            LOGGER.info("User deleted with id: {}", userId);
        } else {
            throw ServiceUtils.logAndThrowUserNotFoundException(userId);
        }
    }

    private void updateUserEntity(User user, UserRequestDto userRequestDto) {
        user.setFirstName(userRequestDto.firstName());
        user.setLastName(userRequestDto.lastName());
    }

    private UserResponseDto convertToUserResponseDto(User user) {
        return new UserResponseDto(user.getId(), user.getFirstName(), user.getLastName(),
                user.getQuota(), user.getLastLoginTimeUtc());
    }
}
