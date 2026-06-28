package com.example.demo.dto.post;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class PostDetailResponse {

    @JsonProperty("post_id")
    private Long postId;

    private String title;

    private String content;

    private String image;

    @JsonProperty("like_count")
    private int likeCount;

    @JsonProperty("comment_count")
    private int commentCount;

    @JsonProperty("view_count")
    private int viewCount;

    private boolean liked;

    @JsonProperty("created_at")
    private String createdAt;

    @JsonProperty("author_nickname")
    private String authorNickname;

    @JsonProperty("author_profile_image")
    private String authorProfileImage;

    private List<PostDetailCommentResponse> comments;

    public PostDetailResponse(
            Long postId,
            String title,
            String content,
            String image,
            int likeCount,
            int commentCount,
            int viewCount,
            boolean liked,
            String createdAt,
            String authorNickname,
            String authorProfileImage,
            List<PostDetailCommentResponse> comments
    ) {
        this.postId = postId;
        this.title = title;
        this.content = content;
        this.image = image;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
        this.viewCount = viewCount;
        this.liked = liked;
        this.createdAt = createdAt;
        this.authorNickname = authorNickname;
        this.authorProfileImage = authorProfileImage;
        this.comments = comments;
    }

    public Long getPostId() {
        return postId;
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

    public boolean isLiked() {
        return liked;
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

    public List<PostDetailCommentResponse> getComments() {
        return comments;
    }
}