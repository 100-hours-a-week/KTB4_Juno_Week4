package com.example.demo.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SigninResponse {
    @JsonProperty("user_id")
    private Long userId;

    public SigninResponse(Long userId){
        this.userId = userId;
    }

    public Long getUserId(){
        return userId;
    }
}
