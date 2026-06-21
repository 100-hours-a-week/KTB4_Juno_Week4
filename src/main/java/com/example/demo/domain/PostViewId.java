package com.example.demo.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class PostViewId implements Serializable {

    @Column(name = "post_id")
    private Long postId;

    @Column(name = "user_id")
    private Long userId;

    protected PostViewId() {
    }

    public PostViewId(Long postId, Long userId) {
        this.postId = postId;
        this.userId = userId;
    }

    public Long getPostId() {
        return postId;
    }

    public Long getUserId() {
        return userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof PostViewId that)) {
            return false;
        }

        return Objects.equals(postId, that.postId)
                && Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(postId, userId);
    }
}