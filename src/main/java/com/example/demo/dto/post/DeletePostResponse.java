package com.example.demo.dto.post;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DeletePostResponse {

    @JsonProperty("post_id")
    private Long postId;

    public DeletePostResponse(Long postId) {
        this.postId = postId;
    }

    public Long getPostId() {
        return postId;
    }
}