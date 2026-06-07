package com.example.demo.domain;

import java.time.LocalDateTime;

public class Comment {

    private Long commentId;
    private Long postId;
    private Long authorId;
    private String content;
    private LocalDateTime createdAt;

    public Comment(Long commentId, Long postId, Long authorId, String content) {
        this.commentId = commentId;
        this.postId = postId;
        this.authorId = authorId;
        this.content = content;
        this.createdAt = LocalDateTime.now();
    }

    public Long getCommentId() {
        return commentId;
    }

    public Long getPostId() {
        return postId;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void update(String content) {
        this.content = content;
    }
}