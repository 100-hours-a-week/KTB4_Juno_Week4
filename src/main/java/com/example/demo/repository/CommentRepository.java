package com.example.demo.repository;

import com.example.demo.domain.Comment;
import com.example.demo.domain.Post;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByPost(Post post);

    @EntityGraph(attributePaths = "author")
    List<Comment> findAllByPostAndDeletedAtIsNull(Post post);
}