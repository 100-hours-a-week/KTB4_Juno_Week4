package com.example.demo.repository;

import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.Set;

@Repository
public class LikeRepository {

    private final Set<String> likes = new HashSet<>();

    public boolean existsByPostIdAndUserId(Long postId, Long userId) {
        return likes.contains(createKey(postId, userId));
    }

    public void save(Long postId, Long userId) {
        likes.add(createKey(postId, userId));
    }

    public void delete(Long postId, Long userId) {
        likes.remove(createKey(postId, userId));
    }

    private String createKey(Long postId, Long userId) {
        return postId + ":" + userId;
    }

    public void deleteAllByPostId(Long postId) {
        likes.removeIf(like -> like.startsWith(postId + ":"));
    }
}