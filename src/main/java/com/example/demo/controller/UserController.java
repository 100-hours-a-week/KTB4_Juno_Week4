package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.dto.user.*;
import com.example.demo.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<SignupResponse>> signup(
            @Valid @RequestBody SignupRequest request
    ) {
        SignupResponse response = userService.signup(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("회원가입에 성공하였습니다.", response));
    }

    @PostMapping("/signin")
    public ResponseEntity<ApiResponse<SigninResponse>> signin(
            @Valid @RequestBody SigninRequest request
    ) {
        SigninResponse response = userService.signin(request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success("로그인에 성공하였습니다.", response));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserInfoResponse>> getMyInfo(
            @RequestHeader(value = "user_id", required = false) Long userId
    ) {
        UserInfoResponse response = userService.getMyInfo(userId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success("회원정보 조회에 성공하였습니다.", response));
    }

    @PatchMapping("/me")
    public ResponseEntity<ApiResponse<UpdateUserInfoResponse>> updateUserInfo(
            @RequestHeader(value = "user_id", required = false) Long userId,
            @Valid @RequestBody UpdateUserInfoRequest request
    ) {
        UpdateUserInfoResponse response = userService.updateUserInfo(userId, request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success("회원정보 수정에 성공하였습니다.", response));
    }

    @PutMapping("/me/password")
    public ResponseEntity<ApiResponse<Void>> updatePassword(
            @RequestHeader(value = "user_id", required = false) Long userId,
            @Valid @RequestBody UpdatePasswordRequest request
    ) {
        userService.updatePassword(userId, request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success("비밀번호 수정에 성공하였습니다.", null));
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteUser(
            @RequestHeader(value = "user_id", required = false) Long userId
    ) {
        userService.deleteUser(userId);

        return ResponseEntity.noContent().build();
    }


    @PostMapping("/signout")
    public ResponseEntity<Void> signout(
            @RequestHeader(value = "user_id", required = false)Long userId
    ){
        userService.signout(userId);
        return ResponseEntity.noContent().build();
    }
}
