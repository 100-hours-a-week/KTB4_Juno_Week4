package com.example.demo.domain;

import java.time.LocalDateTime;

public class Post {

    private Long postId;
    private Long authorId;
    private String title;
    private String content;
    private String image;
    private int likeCount;
    private int commentCount;
    private int viewCount;
    private LocalDateTime createdAt;

    public Post(Long postId, Long authorId, String title, String content, String image) {
        this.postId = postId;
        this.authorId = authorId;
        this.title = title;
        this.content = content;
        this.image = image;
        this.likeCount = 0;
        this.commentCount = 0;
        this.viewCount = 0;
        this.createdAt = LocalDateTime.now();
    }

    public Long getPostId() {
        return postId;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getImage() {
        return image;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public int getViewCount() {
        return viewCount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void increaseViewCount() {
        this.viewCount++;
    }

    public void update(String title, String content, String image) {
        this.title = title;
        this.content = content;
        this.image = image;
    }

    public void increaseCommentCount() {
        this.commentCount++;
    }

    public void decreaseCommentCount() {
        if (this.commentCount > 0) {
            this.commentCount--;
        }
    }

    public void increaseLikeCount() {
        this.likeCount++;
    }
}