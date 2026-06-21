package com.example.demo.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "post_views")
public class PostView {

    @EmbeddedId
    private PostViewId id;

    @MapsId("postId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @MapsId("userId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "last_viewed_at", nullable = false)
    private LocalDateTime lastViewedAt;

    protected PostView() {
    }

    public PostView(Post post, User user) {
        this.post = post;
        this.user = user;
        this.id = new PostViewId(post.getPostId(), user.getUserId());
        this.lastViewedAt = LocalDateTime.now();
    }

    public PostViewId getId() {
        return id;
    }

    public Post getPost() {
        return post;
    }

    public User getUser() {
        return user;
    }

    public LocalDateTime getLastViewedAt() {
        return lastViewedAt;
    }

    public boolean canIncreaseViewCountAfter(LocalDateTime standardTime) {
        return lastViewedAt.isBefore(standardTime);
    }

    public void updateLastViewedAt() {
        this.lastViewedAt = LocalDateTime.now();
    }
}