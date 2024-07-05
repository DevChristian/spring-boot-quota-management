package com.devchristian.quota_management.repository;

import com.devchristian.quota_management.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

@Repository
public class DynamicUserRepository implements UserRepository {

    private final MySqlUserRepository mySqlUserRepository;
    private final ElasticUserRepository elasticUserRepository;

    @Value("${app.mysql-time.start}")
    private int mySqlTimeStart;

    @Value("${app.mysql-time.end}")
    private int mySqlTimeEnd;

    @Autowired
    public DynamicUserRepository(MySqlUserRepository mySqlUserRepository,
                                 ElasticUserRepository elasticUserRepository) {
        this.mySqlUserRepository = mySqlUserRepository;
        this.elasticUserRepository = elasticUserRepository;
    }

    private boolean isMySqlTime() {
        LocalTime now = LocalTime.now(ZoneOffset.UTC);
        return now.isAfter(LocalTime.of(mySqlTimeStart, 0))
                && now.isBefore(LocalTime.of(mySqlTimeEnd, 0));
    }

    private UserRepository getCurrentRepository() {
        if (isMySqlTime()) {
            return mySqlUserRepository;
        } else {
            return elasticUserRepository;
        }
    }

    @Override
    public User save(User user) {
        return getCurrentRepository().save(user);
    }

    @Override
    public Optional<User> findById(String userId) {
        return getCurrentRepository().findById(userId);
    }

    @Override
    public void deleteById(String userId) {
        getCurrentRepository().deleteById(userId);
    }

    @Override
    public boolean existsById(String userId) {
        return getCurrentRepository().existsById(userId);
    }

    @Override
    public List<User> findAll() {
        return getCurrentRepository().findAll();
    }

    @Override
    public void resetAllUserQuotas() {
        getCurrentRepository().resetAllUserQuotas();
    }
}
