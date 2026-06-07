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

import com.example.demo.domain.Comment;
import com.example.demo.dto.post.PostDetailCommentResponse;
import com.example.demo.dto.post.PostDetailResponse;
import com.example.demo.repository.CommentRepository;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final LoginSessionRepository loginSessionRepository;
    private final CommentRepository commentRepository;

    public PostService(
            PostRepository postRepository,
            UserRepository userRepository,
            LoginSessionRepository loginSessionRepository,
            CommentRepository commentRepository
    ) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.loginSessionRepository = loginSessionRepository;
        this.commentRepository = commentRepository;
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

    public PostDetailResponse getPost(Long postId) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        Post post = postRepository.findByPostId(postId)
                .orElseThrow(() -> new ApiException(
                        HttpStatus.NOT_FOUND,
                        "게시글을 찾을 수 없습니다."
                ));

        post.increaseViewCount();

        User author = userRepository.findByUserId(post.getAuthorId())
                .orElseThrow(() -> new ApiException(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "서버 내부 오류가 발생하였습니다."
                ));

        List<PostDetailCommentResponse> comments = commentRepository.findAllByPostId(postId)
                .stream()
                .map(comment -> {
                    User commentAuthor = userRepository.findByUserId(comment.getAuthorId())
                            .orElseThrow(() -> new ApiException(
                                    HttpStatus.INTERNAL_SERVER_ERROR,
                                    "서버 내부 오류가 발생하였습니다."
                            ));

                    return new PostDetailCommentResponse(
                            comment.getCommentId(),
                            comment.getContent(),
                            comment.getCreatedAt().format(formatter),
                            commentAuthor.getNickname(),
                            commentAuthor.getProfileImage()
                    );
                })
                .toList();

        return new PostDetailResponse(
                post.getPostId(),
                post.getTitle(),
                post.getContent(),
                post.getImage(),
                post.getLikeCount(),
                post.getCommentCount(),
                post.getViewCount(),
                post.getCreatedAt().format(formatter),
                author.getNickname(),
                author.getProfileImage(),
                comments
        );
    }
}