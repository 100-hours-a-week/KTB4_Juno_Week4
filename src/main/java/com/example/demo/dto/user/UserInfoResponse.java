package com.example.demo.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserInfoResponse {

    @JsonProperty("user_id")
    private final Long userId;

    private final String email;

    private final String nickname;

    @JsonProperty("profile_image")
    private final String profileImage;

    public UserInfoResponse(Long userId, String email, String nickname, String profileImage) {
        this.userId = userId;
        this.email = email;
        this.nickname = nickname;
        this.profileImage = profileImage;
    }

    public Long getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public String getNickname() {
        return nickname;
    }

    public String getProfileImage() {
        return profileImage;
    }
}