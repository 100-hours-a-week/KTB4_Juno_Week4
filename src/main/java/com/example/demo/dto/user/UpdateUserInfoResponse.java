package com.example.demo.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UpdateUserInfoResponse {

    private String nickname;
    @JsonProperty("profile_image")
    public String profileImage;

    public UpdateUserInfoResponse(String nickname, String profileImage){
        this.nickname = nickname;
        this.profileImage = profileImage;
    }

    public String getNickname(){
        return nickname;
    }

    public String getProfileImage(){
        return profileImage;
    }
}
