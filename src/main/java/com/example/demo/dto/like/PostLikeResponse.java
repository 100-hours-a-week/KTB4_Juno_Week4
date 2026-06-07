package com.example.demo.dto.like;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PostLikeResponse {

    @JsonProperty("post_id")
    private Long postId;

    @JsonProperty("like_count")
    private int likeCount;

    private boolean liked;

    public PostLikeResponse(Long postId, int likeCount, boolean liked) {
        this.postId = postId;
        this.likeCount = likeCount;
        this.liked = liked;
    }

    public Long getPostId() {
        return postId;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public boolean isLiked() {
        return liked;
    }
}