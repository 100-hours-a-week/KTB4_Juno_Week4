package com.example.demo.repository;

import com.example.demo.domain.Post;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class PostRepository {

    private final Map<Long, Post> posts = new LinkedHashMap<>();
    private Long sequence = 1L;

    public Post save(Long authorId, String title, String content, String image) {
        Post post = new Post(sequence, authorId, title, content, image);

        posts.put(sequence, post);
        sequence++;

        return post;
    }

    public Optional<Post> findByPostId(Long postId) {
        return Optional.ofNullable(posts.get(postId));
    }

    public List<Post> findAll() {
        return new ArrayList<>(posts.values());
    }
}