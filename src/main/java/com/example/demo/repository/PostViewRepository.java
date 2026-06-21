package com.example.demo.repository;

import com.example.demo.domain.Post;
import com.example.demo.domain.PostView;
import com.example.demo.domain.PostViewId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostViewRepository extends JpaRepository<PostView, PostViewId> {

    void deleteAllByPost(Post post);
}