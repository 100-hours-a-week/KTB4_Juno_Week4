package com.example.demo.dto.comment;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DeleteCommentResponse {

    @JsonProperty("comment_id")
    private Long commentId;

    public DeleteCommentResponse(Long commentId) {
        this.commentId = commentId;
    }

    public Long getCommentId() {
        return commentId;
    }
}