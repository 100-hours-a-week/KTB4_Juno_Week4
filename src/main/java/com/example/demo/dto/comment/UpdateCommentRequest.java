package com.example.demo.dto.comment;

import jakarta.validation.constraints.NotBlank;

public class UpdateCommentRequest {

    @NotBlank(message = "댓글 내용을 입력해주세요.")
    private String content;

    public String getContent() {
        return content;
    }
}