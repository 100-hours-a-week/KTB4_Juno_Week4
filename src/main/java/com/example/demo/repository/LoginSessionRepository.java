package com.example.demo.repository;

import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.Set;

@Repository
public class LoginSessionRepository {
    private final Set<Long> signedInUserIds = new HashSet<>();

    public void signin(Long userId){
        signedInUserIds.add(userId);
    }

    public void signout(Long userId){
        signedInUserIds.remove(userId);
    }

    public boolean isSignedIn(Long userId){
        return signedInUserIds.contains(userId);
    }
}
