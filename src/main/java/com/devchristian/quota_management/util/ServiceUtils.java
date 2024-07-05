package com.devchristian.quota_management.util;

import com.devchristian.quota_management.exception.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServiceUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceUtils.class);

    public static UserNotFoundException logAndThrowUserNotFoundException(String userId) {
        LOGGER.error("User not found with id: {}", userId);
        return new UserNotFoundException("User not found with id: " + userId);
    }
}
