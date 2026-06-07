package com.example.demo.service;

import com.example.demo.domain.Post;
import com.example.demo.dto.post.CreatePostRequest;
import com.example.demo.dto.post.CreatePostResponse;
import com.example.demo.exception.ApiException;
import com.example.demo.repository.LoginSessionRepository;
import com.example.demo.repository.PostRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final LoginSessionRepository loginSessionRepository;

    public PostService(
            PostRepository postRepository,
            UserRepository userRepository,
            LoginSessionRepository loginSessionRepository
    ) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.loginSessionRepository = loginSessionRepository;
    }

    public CreatePostResponse createPost(Long userId, CreatePostRequest request) {
        validateSignedInUser(userId);
        validateCreatePostRequest(request);

        userRepository.findByUserId(userId)
                .orElseThrow(() -> new ApiException(
                        HttpStatus.UNAUTHORIZED,
                        "로그인이 필요합니다."
                ));

        Post post = postRepository.save(
                userId,
                request.getTitle(),
                request.getContent(),
                request.getImage()
        );

        return new CreatePostResponse(post.getPostId());
    }

    private void validateSignedInUser(Long userId) {
        if (userId == null || !loginSessionRepository.isSignedIn(userId)) {
            throw new ApiException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.");
        }
    }

    private void validateCreatePostRequest(CreatePostRequest request) {
        if (request == null
                || isBlank(request.getTitle())
                || isBlank(request.getContent())) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "잘못된 요청입니다.");
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}