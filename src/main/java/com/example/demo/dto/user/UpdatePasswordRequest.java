package com.example.demo.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UpdatePasswordRequest {
    @JsonProperty("current_password")
    private String currentPassword;

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
