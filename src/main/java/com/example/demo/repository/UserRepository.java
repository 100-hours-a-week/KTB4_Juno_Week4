package com.example.demo.repository;

import com.example.demo.domain.User;
import org.springframework.stereotype.Repository;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

//회원을 저장하거나 찾는 저장소
@Repository
public class UserRepository {

    private final Map<Long, User> users = new LinkedHashMap<>();
    private Long sequence = 1L;

//    새로운 user 객체 만들고 저장
    public User save(String email, String password, String nickname, String profileImage) {
    User user = new User(sequence, email, password, nickname, profileImage);

    users.put(sequence, user);
    sequence++;

    return user;
}
    // 현재 로그인 한 사용자를 찾는 메서드
    public Optional<User> findByUserId(Long userId){
        return Optional.ofNullable(users.get(userId));
    }
    // 저장된 회원들 중에서 같은 이메일, 닉네임 중복 체크
    public Optional<User> findByEmail(String email){
        return users.values().stream().filter(user -> user.getEmail().equals(email)).findFirst();
    }

    public Optional<User> findByNickname(String nickname){
        return users.values().stream().filter(user -> user.getNickname().equals(nickname)).findFirst();
    }

    public void deleteByUserId(Long userId){
        users.remove(userId);
    }
}
