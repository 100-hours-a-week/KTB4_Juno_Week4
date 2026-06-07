package com.example.demo.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/posts")
public class PostController {

    @GetMapping
    public String getPostList(){
        return "게시글 목록 조회 API";
    }

    @GetMapping("/{post_id}")
    public String getPost(){
        return "게시글 조회 API";
    }

    @PostMapping
    public String createPost() {
        return "게시글 작성 API";
    }

    @PatchMapping("/{post_id}")
    public String updatePost(){
        return "게시글 수정 API";
    }

    @DeleteMapping("/{post_id}")
    public String deletePost(){
        return "게시글 삭제 API";
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
