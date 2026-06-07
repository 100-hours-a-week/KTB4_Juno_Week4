package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.dto.user.*;
import com.example.demo.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<SignupResponse>> signup(@RequestBody SignupRequest request){
        SignupResponse response = userService.signup(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("회원가입에 성공하였습니다.", response));
    }

    @PostMapping("/signin")
    public ResponseEntity<ApiResponse<SigninResponse>> signin(@RequestBody SigninRequest request){
        SigninResponse response = userService.signin(request);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("로그인에 성공하였습니다.", response));
    }

    @PatchMapping("/me")
    // 로그인한 사용자만 회원정보를 수정할 수 있기 때문에 header에 user_id 담아서 구분
    public ResponseEntity<ApiResponse<UpdateUserInfoResponse>> updateUserInfo(@RequestHeader(value = "user_id", required = false) Long userId, @RequestBody UpdateUserInfoRequest request)
    {
        UpdateUserInfoResponse response = userService.updateUserInfo(userId, request);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("회원정보 수정에 성공하였습니다.", response));
    }

    @PutMapping("/me/password")
    public ResponseEntity<ApiResponse<Void>> updatePassword(
            @RequestHeader(value = "user_id", required = false) Long userId,
            @RequestBody UpdatePasswordRequest request
    ){
        userService.updatePassword(userId, request);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("비밀번호 수정에 성공하였습니다.", null));
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteUser(
            @RequestHeader(value = "user_id", required = false) Long userId,
            @RequestBody DeleteUserRequest request
    ) {
        userService.deleteUser(userId, request);

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
