package com.example.demo.service;

import com.example.demo.domain.Post;
import com.example.demo.domain.User;
import com.example.demo.dto.like.PostLikeResponse;
import com.example.demo.dto.post.*;
import com.example.demo.exception.ApiException;
import com.example.demo.repository.*;
import com.example.demo.domain.Comment;
import com.example.demo.dto.comment.UpdateCommentRequest;
import com.example.demo.dto.comment.UpdateCommentResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.example.demo.dto.comment.CreateCommentRequest;
import com.example.demo.dto.comment.CreateCommentResponse;

import com.example.demo.dto.comment.DeleteCommentResponse;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final LoginSessionRepository loginSessionRepository;
    private final CommentRepository commentRepository;
    private final LikeRepository likeRepository;
    private final PostViewRepository postViewRepository;


    public PostService(
            PostRepository postRepository,
            UserRepository userRepository,
            LoginSessionRepository loginSessionRepository,
            CommentRepository commentRepository,
            LikeRepository likeRepository,
            PostViewRepository postViewRepository
    ) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.loginSessionRepository = loginSessionRepository;
        this.commentRepository = commentRepository;
        this.likeRepository = likeRepository;
        this.postViewRepository = postViewRepository;
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
                    User author = findUserOrNull(post.getAuthorId());

                    return new PostListItemResponse(
                            post.getPostId(),
                            post.getTitle(),
                            post.getLikeCount(),
                            post.getCommentCount(),
                            post.getViewCount(),
                            post.getCreatedAt().format(formatter),
                            getDisplayNickname(author),
                            getDisplayProfileImage(author)
                    );
                })
                .toList();

        return new PostListResponse(posts);
    }

    public PostDetailResponse getPost(Long userId, Long postId) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        Post post = postRepository.findByPostId(postId)
                .orElseThrow(() -> new ApiException(
                        HttpStatus.NOT_FOUND,
                        "게시글을 찾을 수 없습니다."
                ));

        increaseViewCountIfNeeded(userId, post);

        User author = findUserOrNull(post.getAuthorId());

        List<PostDetailCommentResponse> comments = commentRepository.findAllByPostId(postId)
                .stream()
                .map(comment -> {
                    User commentAuthor = findUserOrNull(comment.getAuthorId());

                    return new PostDetailCommentResponse(
                            comment.getCommentId(),
                            comment.getContent(),
                            comment.getCreatedAt().format(formatter),
                            getDisplayNickname(commentAuthor),
                            getDisplayProfileImage(commentAuthor)
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
                getDisplayNickname(author),
                getDisplayProfileImage(author),
                comments
        );
    }

    public UpdatePostResponse updatePost(Long userId, Long postId, UpdatePostRequest request) {
        validateSignedInUser(userId);
        validateUpdatePostRequest(request);

        Post post = postRepository.findByPostId(postId)
                .orElseThrow(() -> new ApiException(
                        HttpStatus.NOT_FOUND,
                        "게시글을 찾을 수 없습니다."
                ));

        if (!post.getAuthorId().equals(userId)) {
            throw new ApiException(
                    HttpStatus.FORBIDDEN,
                    "게시글 수정 권한이 없습니다."
            );
        }

        post.update(
                request.getTitle(),
                request.getContent(),
                request.getImage()
        );

        return new UpdatePostResponse(post.getPostId());
    }

    private void validateUpdatePostRequest(UpdatePostRequest request) {
        if (request == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "잘못된 요청입니다.");
        }

        boolean hasTitle = request.getTitle() != null;
        boolean hasContent = request.getContent() != null;
        boolean hasImage = request.getImage() != null;

        if (!hasTitle && !hasContent && !hasImage) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "수정할 내용을 입력해주세요.");
        }

        if (hasTitle && isBlank(request.getTitle())) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "제목을 올바르게 입력해주세요.");
        }

        if (hasContent && isBlank(request.getContent())) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "내용을 올바르게 입력해주세요.");
        }
    }

    public DeletePostResponse deletePost(Long userId, Long postId) {
        validateSignedInUser(userId);

        Post post = postRepository.findByPostId(postId)
                .orElseThrow(() -> new ApiException(
                        HttpStatus.NOT_FOUND,
                        "게시글을 찾을 수 없습니다."
                ));

        if (!post.getAuthorId().equals(userId)) {
            throw new ApiException(
                    HttpStatus.FORBIDDEN,
                    "게시글 삭제 권한이 없습니다."
            );
        }

        commentRepository.deleteAllByPostId(postId);
        likeRepository.deleteAllByPostId(postId);
        postViewRepository.deleteAllByPostId(postId);
        postRepository.deleteByPostId(postId);

        return new DeletePostResponse(postId);
    }

    public CreateCommentResponse createComment(
            Long userId,
            Long postId,
            CreateCommentRequest request
    ) {
        validateSignedInUser(userId);
        validateCreateCommentRequest(request);

        Post post = postRepository.findByPostId(postId)
                .orElseThrow(() -> new ApiException(
                        HttpStatus.NOT_FOUND,
                        "게시글을 찾을 수 없습니다."
                ));

        userRepository.findByUserId(userId)
                .orElseThrow(() -> new ApiException(
                        HttpStatus.UNAUTHORIZED,
                        "로그인이 필요합니다."
                ));

        Comment comment = commentRepository.save(
                postId,
                userId,
                request.getContent()
        );

        post.increaseCommentCount();

        return new CreateCommentResponse(comment.getCommentId());
    }

    private void validateCreateCommentRequest(CreateCommentRequest request) {
        if (request == null || isBlank(request.getContent())) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "잘못된 요청입니다.");
        }
    }

    public UpdateCommentResponse updateComment(
            Long userId,
            Long postId,
            Long commentId,
            UpdateCommentRequest request
    ) {
        validateSignedInUser(userId);
        validateUpdateCommentRequest(request);

        Comment comment = commentRepository.findByCommentIdAndPostId(commentId, postId)
                .orElseThrow(() -> new ApiException(
                        HttpStatus.NOT_FOUND,
                        "댓글을 찾을 수 없습니다."
                ));

        if (!comment.getAuthorId().equals(userId)) {
            throw new ApiException(
                    HttpStatus.FORBIDDEN,
                    "댓글 수정 권한이 없습니다."
            );
        }

        comment.update(request.getContent());

        return new UpdateCommentResponse(comment.getCommentId());
    }

    private void validateUpdateCommentRequest(UpdateCommentRequest request) {
        if (request == null || isBlank(request.getContent())) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "잘못된 요청입니다.");
        }
    }

    public DeleteCommentResponse deleteComment(
            Long userId,
            Long postId,
            Long commentId
    ) {
        validateSignedInUser(userId);

        Comment comment = commentRepository.findByCommentIdAndPostId(commentId, postId)
                .orElseThrow(() -> new ApiException(
                        HttpStatus.NOT_FOUND,
                        "댓글을 찾을 수 없습니다."
                ));

        if (!comment.getAuthorId().equals(userId)) {
            throw new ApiException(
                    HttpStatus.FORBIDDEN,
                    "댓글 삭제 권한이 없습니다."
            );
        }

        Post post = postRepository.findByPostId(postId)
                .orElseThrow(() -> new ApiException(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "서버 내부 오류가 발생하였습니다."
                ));

        commentRepository.deleteByCommentId(commentId);
        post.decreaseCommentCount();

        return new DeleteCommentResponse(commentId);
    }

    public PostLikeResponse createLike(Long userId, Long postId) {
        validateSignedInUser(userId);

        Post post = postRepository.findByPostId(postId)
                .orElseThrow(() -> new ApiException(
                        HttpStatus.NOT_FOUND,
                        "게시글을 찾을 수 없습니다."
                ));

        userRepository.findByUserId(userId)
                .orElseThrow(() -> new ApiException(
                        HttpStatus.UNAUTHORIZED,
                        "로그인이 필요합니다."
                ));

        boolean alreadyLiked = likeRepository.existsByPostIdAndUserId(postId, userId);

        if (!alreadyLiked) {
            likeRepository.save(postId, userId);
            post.increaseLikeCount();
        }

        return new PostLikeResponse(
                post.getPostId(),
                post.getLikeCount(),
                true
        );
    }

    public PostLikeResponse deleteLike(Long userId, Long postId) {
        validateSignedInUser(userId);

        Post post = postRepository.findByPostId(postId)
                .orElseThrow(() -> new ApiException(
                        HttpStatus.NOT_FOUND,
                        "게시글을 찾을 수 없습니다."
                ));

        userRepository.findByUserId(userId)
                .orElseThrow(() -> new ApiException(
                        HttpStatus.UNAUTHORIZED,
                        "로그인이 필요합니다."
                ));

        boolean alreadyLiked = likeRepository.existsByPostIdAndUserId(postId, userId);

        if (alreadyLiked) {
            likeRepository.delete(postId, userId);
            post.decreaseLikeCount();
        }

        return new PostLikeResponse(
                post.getPostId(),
                post.getLikeCount(),
                false
        );
    }

    private User findUserOrNull(Long userId) {
        return userRepository.findByUserId(userId).orElse(null);
    }

    private String getDisplayNickname(User user) {
        if (user == null) {
            return "탈퇴한 사용자";
        }

        return user.getNickname();
    }

    private String getDisplayProfileImage(User user) {
        if (user == null) {
            return null;
        }

        return user.getProfileImage();
    }

    private void increaseViewCountIfNeeded(Long userId, Post post) {
        if (userId == null || !loginSessionRepository.isSignedIn(userId)) {
            post.increaseViewCount();
            return;
        }

        boolean canIncrease = postViewRepository.canIncreaseViewCount(
                post.getPostId(),
                userId,
                Duration.ofHours(1)
        );

        if (canIncrease) {
            post.increaseViewCount();
        }
    }
}