package com.example.demo.repository;

import com.example.demo.domain.Post;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    @EntityGraph(attributePaths = "author")
    List<Post> findAllByDeletedAtIsNullOrderByCreatedAtDesc();

    @Modifying
    @Query("update Post p set p.likeCount = p.likeCount + 1 where p.postId = :postId")
    int increaseLikeCount(@Param("postId") Long postId);

    @Modifying
    @Query("update Post p set p.likeCount = p.likeCount - 1 where p.postId = :postId and p.likeCount > 0")
    int decreaseLikeCount(@Param("postId") Long postId);

    @Modifying
    @Query("update Post p set p.viewCount = p.viewCount + 1 where p.postId = :postId")
    int increaseViewCount(@Param("postId") Long postId);

    @Modifying
    @Query("update Post p set p.commentCount = p.commentCount + 1 where p.postId = :postId")
    int increaseCommentCount(@Param("postId") Long postId);

    @Modifying
    @Query("update Post p set p.commentCount = p.commentCount - 1 where p.postId = :postId and p.commentCount > 0")
    int decreaseCommentCount(@Param("postId") Long postId);

    @Query("select p.likeCount from Post p where p.postId = :postId")
    int findLikeCountByPostId(@Param("postId") Long postId);

    @Query("select p.viewCount from Post p where p.postId = :postId")
    int findViewCountByPostId(@Param("postId") Long postId);
}