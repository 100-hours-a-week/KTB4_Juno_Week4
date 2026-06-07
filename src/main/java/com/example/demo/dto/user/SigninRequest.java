package com.example.demo.dto.user;

public class SigninRequest {

    private String email;
    private String password;

    public SigninRequest(){
    }

    public String getEmail(){
        return email;
    }

    public String getPassword(){
        return password;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public void setPassword(String password){
        this.password = password;
    }
}
