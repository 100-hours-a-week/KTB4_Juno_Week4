package com.example.demo.exception;

import com.example.demo.dto.ApiResponse;
// http 상태 코드와 응답 body를 함게 정할 수 있게 해주는 메서드
import org.springframework.http.ResponseEntity;
//특정 예외가 발생했을 때 어떤 메서드가 처리할 지 정해주는 어노테이션
import org.springframework.web.bind.annotation.ExceptionHandler;
// 전체 컨트롤러에서 발생하는 예외를 한 곳에서 처리하겠다는 뜻
import org.springframework.web.bind.annotation.RestControllerAdvice;

// Controller에서 예외가 발생하면 이 클래스에서 처리하겠다는 뜻
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiResponse<Void>> handleApiException(ApiException e) {
        return ResponseEntity.status(e.getStatus()).body(ApiResponse.error(e.getMessage()));
    }

    // 예상치 못한 모든 에러를 처리하기 위한 코드
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(Exception e){
        return ResponseEntity.internalServerError().body(ApiResponse.error("서버 내부 오류가 발생하였습니다."));
    }
}


