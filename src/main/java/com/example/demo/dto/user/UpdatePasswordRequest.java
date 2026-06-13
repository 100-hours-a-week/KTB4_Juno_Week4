package com.example.demo.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class UpdatePasswordRequest {

    @NotBlank(message = "현재 비밀번호를 입력해주세요.")
    @JsonProperty("current_password")
    private String currentPassword;

    @NotBlank(message = "새 비밀번호를 입력해주세요.")
    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d).{8,}$",
            message = "비밀번호는 영문과 숫자를 포함해 8자 이상이어야 합니다."
    )
    @JsonProperty("new_password")
    private String newPassword;

    public UpdatePasswordRequest(){
    }

    public String getCurrentPassword(){
        return currentPassword;
    }

    public String getNewPassword(){
        return newPassword;
    }

    public void setCurrentPassword(String currentPassword){
        this.currentPassword = currentPassword;
    }

    public void setNewPassword(String newPassword){
        this.newPassword = newPassword;
    }
}