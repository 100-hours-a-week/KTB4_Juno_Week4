package com.example.demo.dto.post;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Size;

public class UpdatePostRequest {

    @Size(max = 100, message = "제목은 100자 이하로 입력해주세요.")
    private String title;

    private String content;

    private String image;

    @AssertTrue(message = "수정할 내용을 입력해주세요.")
    public boolean hasUpdateField() {
        return title != null || content != null || image != null;
    }

    @AssertTrue(message = "제목을 올바르게 입력해주세요.")
    public boolean isTitleValid() {
        return title == null || !title.trim().isEmpty();
    }

    @AssertTrue(message = "내용을 올바르게 입력해주세요.")
    public boolean isContentValid() {
        return content == null || !content.trim().isEmpty();
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
}