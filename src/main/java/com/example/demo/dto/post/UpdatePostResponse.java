package com.example.demo.dto.post;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UpdatePostResponse {

    @JsonProperty("post_id")
    private Long postId;

    public UpdatePostResponse(Long postId) {
        this.postId = postId;
    }

    public Long getPostId() {
        return postId;
    }
}