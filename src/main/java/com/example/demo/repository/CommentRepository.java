package com.example.demo.repository;

import com.example.demo.domain.Comment;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class CommentRepository {

    private final Map<Long, Comment> comments = new LinkedHashMap<>();
    private Long sequence = 1L;

    public Comment save(Long postId, Long authorId, String content) {
        Comment comment = new Comment(sequence++, postId, authorId, content);
        comments.put(comment.getCommentId(), comment);

        return comment;
    }

    public List<Comment> findAllByPostId(Long postId) {
        return new ArrayList<>(comments.values())
                .stream()
                .filter(comment -> comment.getPostId().equals(postId))
                .toList();
    }

    public Optional<Comment> findByCommentIdAndPostId(Long commentId, Long postId) {
        return Optional.ofNullable(comments.get(commentId))
                .filter(comment -> comment.getPostId().equals(postId));
    }

    public void deleteByCommentId(Long commentId) {
        comments.remove(commentId);
    }

    public void deleteAllByPostId(Long postId) {
        comments.values().removeIf(comment -> comment.getPostId().equals(postId));
    }
}