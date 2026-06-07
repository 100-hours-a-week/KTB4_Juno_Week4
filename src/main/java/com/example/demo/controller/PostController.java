package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.dto.post.CreatePostRequest;
import com.example.demo.dto.post.CreatePostResponse;
import com.example.demo.dto.post.PostDetailResponse;
import com.example.demo.dto.post.PostListResponse;
import com.example.demo.dto.post.UpdatePostRequest;
import com.example.demo.dto.post.UpdatePostResponse;
import com.example.demo.service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CreatePostResponse>> createPost(
            @RequestHeader(value = "user_id", required = false) Long userId,
            @RequestBody CreatePostRequest request
    ) {
        CreatePostResponse response = postService.createPost(userId, request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("게시글 작성에 성공하였습니다.", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PostListResponse>> getPostList() {
        PostListResponse response = postService.getPostList();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success("게시글 목록 조회에 성공하였습니다.", response));
    }

    @GetMapping("/{post_id}")
    public ResponseEntity<ApiResponse<PostDetailResponse>> getPost(
            @PathVariable("post_id") Long postId
    ) {
        PostDetailResponse response = postService.getPost(postId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success("게시글 상세 조회에 성공하였습니다.", response));
    }

    @PatchMapping("/{post_id}")
    public ResponseEntity<ApiResponse<UpdatePostResponse>> updatePost(
            @RequestHeader(value = "user_id", required = false) Long userId,
            @PathVariable("post_id") Long postId,
            @RequestBody UpdatePostRequest request
    ) {
        UpdatePostResponse response = postService.updatePost(userId, postId, request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success("게시글 수정에 성공하였습니다.", response));
    }

    @DeleteMapping("/{post_id}")
    public ResponseEntity<ApiResponse<Void>> deletePost(
            @RequestHeader(value = "user_id", required = false) Long userId,
            @PathVariable("post_id") Long postId
    ) {
        postService.deletePost(userId, postId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success("게시글 삭제에 성공하였습니다.", null));
    }

    @PostMapping("/{post_id}/comments")
    public String createComment(){
        return "댓글 작성 API";
    }

    @PatchMapping("/{post_id}/comments/{comment_id}")
    public String updateComment(){
        return "댓글 수정 API";
    }

    @DeleteMapping("/{post_id}/comments/{comment_id}")
    public String deleteComment(){
        return "댓글 삭제 API";
    }

    @PostMapping("/{post_id}/likes")
    public String createLike(){
        return "좋아요 수 추가 API";
    }

    @DeleteMapping("/{post_id}/likes")
    public String deleteLike(){
        return "좋아요 수 삭제 API";
    }
}