package com.example.demo.service;

import com.example.demo.domain.Post;
import com.example.demo.domain.User;
import com.example.demo.dto.post.CreatePostRequest;
import com.example.demo.dto.post.CreatePostResponse;
import com.example.demo.exception.ApiException;
import com.example.demo.repository.LoginSessionRepository;
import com.example.demo.repository.PostRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.demo.dto.post.PostListItemResponse;
import com.example.demo.dto.post.PostListResponse;

import java.time.format.DateTimeFormatter;
import java.util.List;

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

    public PostListResponse getPostList() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        List<PostListItemResponse> posts = postRepository.findAll()
                .stream()
                .map(post -> {
                    User author = userRepository.findByUserId(post.getAuthorId())
                            .orElseThrow(() -> new ApiException(
                                    HttpStatus.INTERNAL_SERVER_ERROR,
                                    "서버 내부 오류가 발생하였습니다."
                            ));

                    return new PostListItemResponse(
                            post.getPostId(),
                            post.getTitle(),
                            post.getLikeCount(),
                            post.getCommentCount(),
                            post.getViewCount(),
                            post.getCreatedAt().format(formatter),
                            author.getNickname(),
                            author.getProfileImage()
                    );
                })
                .toList();

        return new PostListResponse(posts);
    }
}