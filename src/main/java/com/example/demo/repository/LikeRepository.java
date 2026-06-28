package com.example.demo.repository;

import com.example.demo.domain.Post;
import com.example.demo.domain.PostLike;
import com.example.demo.domain.PostLikeId;
import com.example.demo.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<PostLike, PostLikeId> {

    void deleteAllByPost(Post post);

    boolean existsByPostAndUser(Post post, User user);
}