package com.example.demo.dto.comment;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateCommentResponse {

    @JsonProperty("comment_id")
    private Long commentId;

    public CreateCommentResponse(Long commentId) {
        this.commentId = commentId;
    }

    public Long getCommentId() {
        return commentId;
    }
}