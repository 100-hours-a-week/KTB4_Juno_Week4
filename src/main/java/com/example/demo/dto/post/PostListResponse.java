package com.example.demo.dto.post;

import java.util.List;

public class PostListResponse {

    private List<PostListItemResponse> posts;

    public PostListResponse(List<PostListItemResponse> posts) {
        this.posts = posts;
    }

    public List<PostListItemResponse> getPosts() {
        return posts;
    }
}