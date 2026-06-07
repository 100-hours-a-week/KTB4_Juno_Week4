package com.example.demo.dto.comment;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UpdateCommentResponse {

    @JsonProperty("comment_id")
    private Long commentId;

    public UpdateCommentResponse(Long commentId) {
        this.commentId = commentId;
    }

    public Long getCommentId() {
        return commentId;
    }
}