package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.dto.comment.*;
import com.example.demo.dto.like.PostLikeResponse;
import com.example.demo.dto.post.*;
import com.example.demo.service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

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
            @Valid @RequestBody CreatePostRequest request
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
            @RequestHeader(value = "user_id", required = false) Long userId,
            @PathVariable("post_id") Long postId
    ) {
        PostDetailResponse response = postService.getPost(userId, postId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success("게시글 상세 조회에 성공하였습니다.", response));
    }

    @PatchMapping("/{post_id}")
    public ResponseEntity<ApiResponse<UpdatePostResponse>> updatePost(
            @RequestHeader(value = "user_id", required = false) Long userId,
            @PathVariable("post_id") Long postId,
            @Valid @RequestBody UpdatePostRequest request
    ) {
        UpdatePostResponse response = postService.updatePost(userId, postId, request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success("게시글 수정에 성공하였습니다.", response));
    }

    @DeleteMapping("/{post_id}")
    public ResponseEntity<ApiResponse<DeletePostResponse>> deletePost(
            @RequestHeader(value = "user_id", required = false) Long userId,
            @PathVariable("post_id") Long postId
    ) {
        DeletePostResponse response = postService.deletePost(userId, postId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success("게시글 삭제에 성공하였습니다.", response));
    }

    @PostMapping("/{post_id}/comments")
    public ResponseEntity<ApiResponse<CreateCommentResponse>> createComment(
            @RequestHeader(value = "user_id", required = false) Long userId,
            @PathVariable("post_id") Long postId,
            @Valid @RequestBody CreateCommentRequest request
    ) {
        CreateCommentResponse response = postService.createComment(userId, postId, request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("댓글 작성에 성공하였습니다.", response));
    }

    @PatchMapping("/{post_id}/comments/{comment_id}")
    public ResponseEntity<ApiResponse<UpdateCommentResponse>> updateComment(
            @RequestHeader(value = "user_id", required = false) Long userId,
            @PathVariable("post_id") Long postId,
            @PathVariable("comment_id") Long commentId,
            @Valid @RequestBody UpdateCommentRequest request
    ) {
        UpdateCommentResponse response = postService.updateComment(userId, postId, commentId, request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success("댓글 수정에 성공하였습니다.", response));
    }

    @DeleteMapping("/{post_id}/comments/{comment_id}")
    public ResponseEntity<ApiResponse<DeleteCommentResponse>> deleteComment(
            @RequestHeader(value = "user_id", required = false) Long userId,
            @PathVariable("post_id") Long postId,
            @PathVariable("comment_id") Long commentId
    ) {
        DeleteCommentResponse response = postService.deleteComment(
                userId,
                postId,
                commentId
        );

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success("댓글 삭제에 성공하였습니다.", response));
    }

    @PostMapping("/{post_id}/likes")
    public ResponseEntity<ApiResponse<PostLikeResponse>> createLike(
            @RequestHeader(value = "user_id", required = false) Long userId,
            @PathVariable("post_id") Long postId
    ) {
        PostLikeResponse response = postService.createLike(userId, postId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success("좋아요가 추가되었습니다.", response));
    }

    @DeleteMapping("/{post_id}/likes")
    public ResponseEntity<ApiResponse<PostLikeResponse>> deleteLike(
            @RequestHeader(value = "user_id", required = false) Long userId,
            @PathVariable("post_id") Long postId
    ) {
        PostLikeResponse response = postService.deleteLike(userId, postId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success("좋아요가 취소되었습니다.", response));
    }
}