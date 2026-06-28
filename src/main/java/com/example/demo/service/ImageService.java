package com.example.demo.service;

import com.example.demo.dto.image.ImageUploadResponse;
import com.example.demo.exception.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

@Service
public class ImageService {

    private static final String UPLOAD_DIR = "uploads/images";

    public ImageUploadResponse uploadImage(MultipartFile image) {
        validateImage(image);

        try {
            Path uploadPath = Path.of(UPLOAD_DIR)
                    .toAbsolutePath()
                    .normalize();

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String originalFilename = image.getOriginalFilename();
            String extension = getExtension(originalFilename);
            String savedFilename = UUID.randomUUID() + extension;

            Path savedPath = uploadPath.resolve(savedFilename)
                    .normalize();

            image.transferTo(savedPath.toFile());

            return new ImageUploadResponse("/uploads/images/" + savedFilename);
        } catch (IOException error) {
            throw new ApiException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "이미지 업로드 중 오류가 발생했습니다."
            );
        }
    }

    private void validateImage(MultipartFile image) {
        if (image == null || image.isEmpty()) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "이미지를 첨부해주세요.");
        }

        String contentType = image.getContentType();

        if (contentType == null || !contentType.startsWith("image/")) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "이미지 파일만 업로드할 수 있습니다.");
        }
    }

    private String getExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            return ".png";
        }

        return filename.substring(filename.lastIndexOf("."));
    }
}