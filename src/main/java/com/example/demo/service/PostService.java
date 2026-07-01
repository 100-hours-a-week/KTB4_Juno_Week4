package com.example.demo.service;

import com.example.demo.domain.Post;
import com.example.demo.domain.User;
import com.example.demo.domain.PostLike;
import com.example.demo.domain.PostLikeId;
import com.example.demo.domain.PostView;
import com.example.demo.domain.PostViewId;
import com.example.demo.dto.like.PostLikeResponse;
import com.example.demo.dto.post.*;
import com.example.demo.exception.ApiException;
import com.example.demo.repository.*;
import com.example.demo.domain.Comment;
import com.example.demo.dto.comment.UpdateCommentRequest;
import com.example.demo.dto.comment.UpdateCommentResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.example.demo.dto.comment.CreateCommentRequest;
import com.example.demo.dto.comment.CreateCommentResponse;
import com.example.demo.dto.comment.DeleteCommentResponse;

@Service
@Transactional
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

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(
                        HttpStatus.UNAUTHORIZED,
                        "로그인이 필요합니다."
                ));

        Post post = postRepository.save(
                new Post(
                        user,
                        request.getTitle(),
                        request.getContent(),
                        request.getImage()
                )
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

        List<PostListItemResponse> posts = postRepository.findAllByDeletedAtIsNullOrderByCreatedAtDesc()
                .stream()
                .map(post -> {
                    User author = post.getAuthor();

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

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ApiException(
                        HttpStatus.NOT_FOUND,
                        "게시글을 찾을 수 없습니다."
                ));

        validateNotDeletedPost(post);

        increaseViewCountIfNeeded(userId, post);

        boolean liked = isLikedByUser(userId, post);

        User author = post.getAuthor();

        List<PostDetailCommentResponse> comments = commentRepository.findAllByPostAndDeletedAtIsNull(post)
                .stream()
                .map(comment -> {
                    User commentAuthor = comment.getAuthor();

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
                liked,
                post.getCreatedAt().format(formatter),
                author.getUserId(),
                getDisplayNickname(author),
                getDisplayProfileImage(author),
                comments
        );
    }

    public UpdatePostResponse updatePost(Long userId, Long postId, UpdatePostRequest request) {
        validateSignedInUser(userId);
        validateUpdatePostRequest(request);

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ApiException(
                        HttpStatus.NOT_FOUND,
                        "게시글을 찾을 수 없습니다."
                ));

        validateNotDeletedPost(post);

        if (!post.getAuthor().getUserId().equals(userId)) {
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

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ApiException(
                        HttpStatus.NOT_FOUND,
                        "게시글을 찾을 수 없습니다."
                ));

        validateNotDeletedPost(post);

        if (!post.getAuthor().getUserId().equals(userId)) {
            throw new ApiException(
                    HttpStatus.FORBIDDEN,
                    "게시글 삭제 권한이 없습니다."
            );
        }

        List<Comment> comments = commentRepository.findAllByPostAndDeletedAtIsNull(post);

        for (Comment comment : comments) {
            comment.delete();
        }

        post.delete();

        return new DeletePostResponse(postId);
    }

    public CreateCommentResponse createComment(
            Long userId,
            Long postId,
            CreateCommentRequest request
    ) {
        validateSignedInUser(userId);

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ApiException(
                        HttpStatus.NOT_FOUND,
                        "게시글을 찾을 수 없습니다."
                ));

        validateNotDeletedPost(post);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(
                        HttpStatus.UNAUTHORIZED,
                        "로그인이 필요합니다."
                ));

        Comment comment = commentRepository.save(
                new Comment(
                        post,
                        user,
                        request.getContent()
                )
        );

        post.increaseCommentCount();

        return new CreateCommentResponse(comment.getCommentId());
    }

    public UpdateCommentResponse updateComment(
            Long userId,
            Long postId,
            Long commentId,
            UpdateCommentRequest request
    ) {
        validateSignedInUser(userId);
        validateUpdateCommentRequest(request);

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ApiException(
                        HttpStatus.NOT_FOUND,
                        "댓글을 찾을 수 없습니다."
                ));

        validateNotDeletedComment(comment);
        validateNotDeletedPost(comment.getPost());

        if (!comment.getPost().getPostId().equals(postId)) {
            throw new ApiException(
                    HttpStatus.NOT_FOUND,
                    "댓글을 찾을 수 없습니다."
            );
        }

        if (!comment.getAuthor().getUserId().equals(userId)) {
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

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ApiException(
                        HttpStatus.NOT_FOUND,
                        "댓글을 찾을 수 없습니다."
                ));

        validateNotDeletedComment(comment);
        validateNotDeletedPost(comment.getPost());

        if (!comment.getPost().getPostId().equals(postId)) {
            throw new ApiException(
                    HttpStatus.NOT_FOUND,
                    "댓글을 찾을 수 없습니다."
            );
        }

        if (!comment.getAuthor().getUserId().equals(userId)) {
            throw new ApiException(
                    HttpStatus.FORBIDDEN,
                    "댓글 삭제 권한이 없습니다."
            );
        }

        Post post = comment.getPost();

        comment.delete();
        post.decreaseCommentCount();

        return new DeleteCommentResponse(commentId);
    }

    public PostLikeResponse createLike(Long userId, Long postId) {
        validateSignedInUser(userId);

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ApiException(
                        HttpStatus.NOT_FOUND,
                        "게시글을 찾을 수 없습니다."
                ));

        validateNotDeletedPost(post);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(
                        HttpStatus.UNAUTHORIZED,
                        "로그인이 필요합니다."
                ));

        PostLikeId likeId = new PostLikeId(postId, userId);

        boolean alreadyLiked = likeRepository.existsById(likeId);

        if (!alreadyLiked) {
            likeRepository.save(new PostLike(post, user));
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

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ApiException(
                        HttpStatus.NOT_FOUND,
                        "게시글을 찾을 수 없습니다."
                ));

        validateNotDeletedPost(post);

        userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(
                        HttpStatus.UNAUTHORIZED,
                        "로그인이 필요합니다."
                ));

        PostLikeId likeId = new PostLikeId(postId, userId);

        boolean alreadyLiked = likeRepository.existsById(likeId);

        if (alreadyLiked) {
            likeRepository.deleteById(likeId);
            post.decreaseLikeCount();
        }

        return new PostLikeResponse(
                post.getPostId(),
                post.getLikeCount(),
                false
        );
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


    private boolean isLikedByUser(Long userId, Post post) {
        if (userId == null || !loginSessionRepository.isSignedIn(userId)) {
            return false;
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(
                        HttpStatus.UNAUTHORIZED,
                        "로그인이 필요합니다."
                ));

        return likeRepository.existsByPostAndUser(post, user);
    }

    private void increaseViewCountIfNeeded(Long userId, Post post) {
        if (userId == null || !loginSessionRepository.isSignedIn(userId)) {
            post.increaseViewCount();
            return;
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(
                        HttpStatus.UNAUTHORIZED,
                        "로그인이 필요합니다."
                ));

        PostViewId postViewId = new PostViewId(post.getPostId(), user.getUserId());

        PostView postView = postViewRepository.findById(postViewId)
                .orElse(null);

        if (postView == null) {
            postViewRepository.save(new PostView(post, user));
            post.increaseViewCount();
            return;
        }

        LocalDateTime standardTime = LocalDateTime.now().minusHours(24);

        if (postView.canIncreaseViewCountAfter(standardTime)) {
            post.increaseViewCount();
            postView.updateLastViewedAt();
        }
    }

    private void validateNotDeletedPost(Post post) {
        if (post.getDeletedAt() != null) {
            throw new ApiException(
                    HttpStatus.NOT_FOUND,
                    "게시글을 찾을 수 없습니다."
            );
        }
    }

    private void validateNotDeletedComment(Comment comment) {
        if (comment.getDeletedAt() != null) {
            throw new ApiException(
                    HttpStatus.NOT_FOUND,
                    "댓글을 찾을 수 없습니다."
            );
        }
    }
}