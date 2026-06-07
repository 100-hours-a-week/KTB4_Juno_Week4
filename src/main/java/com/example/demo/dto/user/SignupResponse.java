package com.example.demo.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SignupResponse {
    @JsonProperty("user_id")
    private Long userId;

    public SignupResponse(Long userId){
        this.userId = userId;
    }

    public Long getUserId(){
        return userId;
    }
}
