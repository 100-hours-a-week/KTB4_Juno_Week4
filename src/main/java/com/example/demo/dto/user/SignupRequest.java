package com.example.demo.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class SignupRequest {

    @NotBlank(message = "이메일을 입력해주세요.")
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    private String email;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d).{8,}$",
            message = "비밀번호는 영문과 숫자를 포함해 8자 이상이어야 합니다."
    )
    private String password;

    @NotBlank(message = "닉네임을 입력해주세요.")
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