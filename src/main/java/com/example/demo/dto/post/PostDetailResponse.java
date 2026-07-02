package com.example.demo.dto.post;

import java.util.List;

public class PostDetailResponse {

    private final Long postId;
    private final String title;
    private final String content;
    private final String image;
    private final int likeCount;
    private final int commentCount;
    private final int viewCount;
    private final boolean liked;
    private final String createdAt;
    private final Long authorId;
    private final String nickname;
    private final String profileImage;
    private final List<PostDetailCommentResponse> comments;

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
            Long authorId,
            String nickname,
            String profileImage,
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
        this.authorId = authorId;
        this.nickname = nickname;
        this.profileImage = profileImage;
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

    public Long getAuthorId() {
        return authorId;
    }

    public String getNickname() {
        return nickname;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public List<PostDetailCommentResponse> getComments() {
        return comments;
    }
}