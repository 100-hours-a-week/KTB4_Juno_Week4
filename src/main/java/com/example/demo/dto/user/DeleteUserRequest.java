package com.example.demo.dto.user;

public class DeleteUserRequest {
    private String password;

    public DeleteUserRequest(){
    }

    public String getPassword(){
        return password;
    }

    public void setPassword(){
        this.password = password;
    }

}
