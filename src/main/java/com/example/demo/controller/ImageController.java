package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.dto.image.ImageUploadResponse;
import com.example.demo.service.ImageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/images")
public class ImageController {

    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ImageUploadResponse>> uploadImage(
            @RequestParam("image") MultipartFile image
    ) {
        ImageUploadResponse response = imageService.uploadImage(image);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("이미지 업로드에 성공하였습니다.", response));
    }
}