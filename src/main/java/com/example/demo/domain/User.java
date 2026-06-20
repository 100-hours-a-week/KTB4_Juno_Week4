package com.example.demo.domain;

import java.time.LocalDateTime;

// 서버 내부에서 관리할 회원 객체
public class User {
    private Long userId;
    private String email;
    private String password;
    private String nickname;
    private String profileImage;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    public User(Long userId, String email, String password, String nickname, String profileImage) {
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.profileImage = profileImage;
        this.createdAt = LocalDateTime.now();
    }

    public Long getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getNickname() {
        return nickname;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    public boolean isDeleted() {
        return deletedAt != null;
    }

    public void updateUserInfo(String nickname, String profileImage) {
        this.nickname = nickname;

        if (profileImage != null) {
            this.profileImage = profileImage;
        }

        this.updatedAt = LocalDateTime.now();
    }

    // 현재 비밀번호가 맞는지 확인하는 메서드
    public boolean isPasswordMatched(String password) {
        return this.password.equals(password);
    }

    // 새 비밀번호로 변경하는 메서드
    public void updatePassword(String newPassword) {
        this.password = newPassword;
        this.updatedAt = LocalDateTime.now();
    }

    // 회원 탈퇴 처리 메서드
    public void withdraw() {
        this.email = "deleted_" + this.userId + "@deleted.local";
        this.nickname = "deleted_" + this.userId;
        this.profileImage = null;
        this.deletedAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
}