package com.example.demo.service;

import com.example.demo.domain.User;
import com.example.demo.dto.user.*;
import com.example.demo.exception.ApiException;
import com.example.demo.repository.LoginSessionRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final LoginSessionRepository loginSessionRepository;

    public UserService(UserRepository userRepository, LoginSessionRepository loginSessionRepository){
        this.userRepository = userRepository;
        this.loginSessionRepository = loginSessionRepository;
    }

    public SignupResponse signup(SignupRequest request){

        userRepository.findByEmail(request.getEmail()).ifPresent(user -> {
            throw new ApiException(HttpStatus.CONFLICT, "중복된 이메일입니다.");
        });

        userRepository.findByNickname(request.getNickname()).ifPresent(user -> {
            throw new ApiException(HttpStatus.CONFLICT, "중복된 닉네임입니다.");
        });

        User user = userRepository.save(
                new User(
                        request.getEmail(),
                        request.getPassword(),
                        request.getNickname(),
                        request.getProfileImage()
                )
        );

        return new SignupResponse(user.getUserId());
    }

    public SigninResponse signin(SigninRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ApiException(
                        HttpStatus.UNAUTHORIZED,
                        "이메일 또는 비밀번호가 일치하지 않습니다."
                ));

        if (!user.isPasswordMatched(request.getPassword())) {
            throw new ApiException(
                    HttpStatus.UNAUTHORIZED,
                    "이메일 또는 비밀번호가 일치하지 않습니다."
            );
        }

        loginSessionRepository.signin(user.getUserId());

        return new SigninResponse(user.getUserId());
    }

    public void signout(Long userId){
        if (userId == null || !loginSessionRepository.isSignedIn(userId)) {
            throw new ApiException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.");
        }

        loginSessionRepository.signout(userId);
    }

    public UpdateUserInfoResponse updateUserInfo(Long userId, UpdateUserInfoRequest request){
        validateSignedInUser(userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(
                        HttpStatus.UNAUTHORIZED,
                        "로그인이 필요합니다."
                ));

        userRepository.findByNickname(request.getNickname()).ifPresent(foundUser -> {
            if (!foundUser.getUserId().equals(userId)) {
                throw new ApiException(HttpStatus.CONFLICT, "중복된 닉네임입니다.");
            }
        });

        user.updateUserInfo(request.getNickname(), request.getProfileImage());

        return new UpdateUserInfoResponse(user.getNickname(), user.getProfileImage());
    }

    private void validateSignedInUser(Long userId){
        if (userId == null || !loginSessionRepository.isSignedIn(userId)) {
            throw new ApiException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.");
        }
    }

    @Transactional(readOnly = true)
    public UserInfoResponse getMyInfo(Long userId) {
        validateSignedInUser(userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(
                        HttpStatus.UNAUTHORIZED,
                        "로그인이 필요합니다."
                ));

        if (user.isDeleted()) {
            throw new ApiException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.");
        }

        return new UserInfoResponse(
                user.getUserId(),
                user.getEmail(),
                user.getNickname(),
                user.getProfileImage()
        );
    }

    public void updatePassword(Long userId, UpdatePasswordRequest request){
        validateSignedInUser(userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(
                        HttpStatus.UNAUTHORIZED,
                        "로그인이 필요합니다."
                ));

        user.updatePassword(request.getNewPassword());
    }

    public void deleteUser(Long userId) {
        validateSignedInUser(userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(
                        HttpStatus.UNAUTHORIZED,
                        "로그인이 필요합니다."
                ));

        user.withdraw();

        loginSessionRepository.signout(userId);
    }

}