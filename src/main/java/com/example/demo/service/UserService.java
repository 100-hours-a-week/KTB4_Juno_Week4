package com.example.demo.service;

import com.example.demo.domain.User;
import com.example.demo.dto.user.*;
import com.example.demo.exception.ApiException;
import com.example.demo.repository.LoginSessionRepository;
import com.example.demo.repository.UserRepository;
//HTTP 상태 코드를 사용하기 위해 가져온 것
import org.springframework.http.HttpStatus;
//@Service 어노테이션을 사용하기 위해 가져옴
import org.springframework.stereotype.Service;


@Service
public class UserService {
    private final UserRepository userRepository;
    private final LoginSessionRepository loginSessionRepository;

    //생성자 주입
    public UserService(UserRepository userRepository, LoginSessionRepository loginSessionRepository){
        this.userRepository = userRepository;
        this.loginSessionRepository = loginSessionRepository;
    }
    // signup 메서드 추가
    public SignupResponse signup(SignupRequest request){
        validateSignupRequest(request);

        userRepository.findByEmail(request.getEmail()).ifPresent(user->{
            throw new ApiException(HttpStatus.CONFLICT, "중복된 이메일입니다.");
        });

        userRepository.findByNickname(request.getNickname()).ifPresent(user -> {
            throw new ApiException(HttpStatus.CONFLICT, "중복된 닉네임입니다.");
        });

        User user = userRepository.save(
                request.getEmail(), request.getPassword(), request.getNickname(), request.getProfileImage()
        );

        return new SignupResponse(user.getUserId());
    }
    private void validateSignupRequest(SignupRequest request) {
        if (request == null || isBlank(request.getEmail()) || isBlank(request.getPassword()) || isBlank(request.getNickname())) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "비정상적인 접근입니다.");
        }
    }

    //signin 메서드 추가
    public SigninResponse signin(SigninRequest request){

        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(()-> new ApiException(HttpStatus.UNAUTHORIZED, "이메일 또는 비밀번호가 일치하지 않습니다."));

        if (user.isDeleted()) {
            throw new ApiException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.");
        }

        if(!user.getPassword().equals(request.getPassword())){
            throw new ApiException(HttpStatus.UNAUTHORIZED, "이메일 또는 비밀번호가 일치하지 않습니다.");
        }
        loginSessionRepository.signin(user.getUserId());
        return new SigninResponse(user.getUserId());
    }

    //signout 메서드 추가
    public void signout(Long userId){
        if(userId == null || !loginSessionRepository.isSignedIn(userId)){
            throw new ApiException(HttpStatus.UNAUTHORIZED,"로그인이 필요합니다.");
        }
        loginSessionRepository.signout(userId);
    }

    //회원정보 수정 메서드 추가
    public UpdateUserInfoResponse updateUserInfo(Long userId, UpdateUserInfoRequest request){
        validateSignedInUser(userId);

        // 현재 사용자 찾는 로직
        User user = userRepository.findByUserId(userId).orElseThrow(()->new ApiException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다."));

        userRepository.findByNickname(request.getNickname()).ifPresent(foundUser -> {
            if(!foundUser.getUserId().equals(userId)){
                throw new ApiException(HttpStatus.CONFLICT, "중복된 닉네임입니다.");
            }
        });

        user.updateUserInfo(request.getNickname(), request.getProfileImage());

        return new UpdateUserInfoResponse(user.getNickname(), user.getProfileImage());
    }

    //회원정보 수정 관련 검증 메서드 추가 - 로그인한 사용자만 수정할 수 있게
    private void validateSignedInUser(Long userId){
        if(userId==null||!loginSessionRepository.isSignedIn(userId)){
            throw new ApiException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.");
        }
    }

    //비밀번호 수정 메서드 추가
    public void updatePassword(Long userId, UpdatePasswordRequest request){
        validateSignedInUser(userId);

        User user = userRepository.findByUserId(userId).orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다."));

        if(!user.isPasswordMatched(request.getCurrentPassword())){
            throw new ApiException(HttpStatus.FORBIDDEN, "현재 비밀번호가 일치하지 않습니다.");
        }
        user.updatePassword(request.getNewPassword());

    }


    // 회원탈퇴 메서드
    public void deleteUser(Long userId, DeleteUserRequest request) {
        validateSignedInUser(userId);

        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다."));

        if (!user.isPasswordMatched(request.getPassword())) {
            throw new ApiException(HttpStatus.FORBIDDEN, "현재 비밀번호가 일치하지 않습니다.");
        }

        user.withdraw();

        // 회원이 탈퇴했으면 로그인 상태도 남아있으면 안 되기 때문에 signedInUserIds에서도 해당 userId를 제거
        loginSessionRepository.signout(userId);
    }


    private boolean isBlank(String value){
        return value == null || value.trim().isEmpty();
    }
}
