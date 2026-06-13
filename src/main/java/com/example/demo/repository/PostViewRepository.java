package com.example.demo.repository;

import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Repository
public class PostViewRepository {

    private final Map<String, LocalDateTime> viewHistories = new HashMap<>();

    public boolean canIncreaseViewCount(Long postId, Long userId, Duration viewInterval) {
        String key = createKey(postId, userId);
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime lastViewedAt = viewHistories.get(key);

        if (lastViewedAt == null || lastViewedAt.plus(viewInterval).isBefore(now)) {
            viewHistories.put(key, now);
            return true;
        }

        return false;
    }

    public void deleteAllByPostId(Long postId) {
        viewHistories.keySet().removeIf(key -> key.startsWith(postId + ":"));
    }

    private String createKey(Long postId, Long userId) {
        return postId + ":" + userId;
    }
}