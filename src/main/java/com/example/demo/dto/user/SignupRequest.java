package com.example.demo.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;

// request로 들어온 회원가입 데이터
public class SignupRequest {

    private String email;
    private String password;
    private String nickname;

    @JsonProperty("profile_image")
    private String profileImage;

    public SignupRequest(){
    }

    public String getEmail(){
        return email;
    }

    public String getPassword(){
        return password;
    }

    public String getNickname(){
        return nickname;
    }

    public String getProfileImage(){
        return profileImage;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public void setNickname(String nickname){
        this.nickname = nickname;
    }

    public void setProfileImage(String profileImage){
        this.profileImage = profileImage;
    }
}
