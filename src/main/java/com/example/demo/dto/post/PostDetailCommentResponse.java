package com.example.demo.dto.post;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PostDetailCommentResponse {

    @JsonProperty("comment_id")
    private Long commentId;

    private String content;

    @JsonProperty("created_at")
    private String createdAt;

    @JsonProperty("author_nickname")
    private String authorNickname;

    @JsonProperty("author_profile_image")
    private String authorProfileImage;

    public PostDetailCommentResponse(
            Long commentId,
            String content,
            String createdAt,
            String authorNickname,
            String authorProfileImage
    ) {
        this.commentId = commentId;
        this.content = content;
        this.createdAt = createdAt;
        this.authorNickname = authorNickname;
        this.authorProfileImage = authorProfileImage;
    }

    public Long getCommentId() {
        return commentId;
    }

    public String getContent() {
        return content;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getAuthorNickname() {
        return authorNickname;
    }

    public String getAuthorProfileImage() {
        return authorProfileImage;
    }
}