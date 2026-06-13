package com.example.demo.dto.user;

import jakarta.validation.constraints.NotBlank;

public class DeleteUserRequest {

    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password;

    public DeleteUserRequest(){
    }

    public String getPassword(){
        return password;
    }

    public void setPassword(String password){
        this.password = password;
    }
}