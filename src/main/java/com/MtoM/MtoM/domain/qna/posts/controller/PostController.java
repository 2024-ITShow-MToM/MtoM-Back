package com.MtoM.MtoM.domain.qna.posts.controller;

import com.MtoM.MtoM.domain.qna.posts.dao.CommentResponse;
import com.MtoM.MtoM.domain.qna.posts.dao.PostHeartUsersResponse;
import com.MtoM.MtoM.domain.qna.posts.dao.PostResponse;
import com.MtoM.MtoM.domain.qna.posts.domain.PostDomain;
import com.MtoM.MtoM.domain.qna.posts.dto.CreatePostDTO;
import com.MtoM.MtoM.domain.qna.posts.dto.UpdatePostDTO;
import com.MtoM.MtoM.domain.qna.posts.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;


    // 모든 게시물 조회
    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getAllPosts() {
        List<Map<String, Object>> postsWithStats = postService.getAllPosts();
        return new ResponseEntity<>(postsWithStats, HttpStatus.OK);
    }

    // 게시물 상세 조회
    @GetMapping("/{id}")
    public ResponseEntity<PostDomain> getPostById(@PathVariable("id") Long id) {
        PostDomain post = postService.getPostById(id);
        return new ResponseEntity<>(post, HttpStatus.OK);
    }

    @GetMapping("/details/{id}")
    public ResponseEntity<PostResponse> getPostDetailsById(@PathVariable("id") Long id) {
        PostResponse response = postService.getPostDetail(id);
        return ResponseEntity.ok(response);
    }

    // 게시물에 대한 댓글 목록 조회
    @GetMapping("/{id}/comments")
    public List<CommentResponse> getPostComments(@PathVariable Long id) {
        return postService.getPostComments(id);
    }

    // 게시물 생성
    @PostMapping
    public ResponseEntity<String> createPost(@ModelAttribute CreatePostDTO postDTO) {
        ResponseEntity<String> response = postService.createPost(postDTO);
        return new ResponseEntity<>(response.getBody(), response.getStatusCode());
    }

    // 게시물 수정
    @PatchMapping("/{id}")
    public ResponseEntity<String> updatePost(@PathVariable("id") Long id, @ModelAttribute UpdatePostDTO postDTO, @RequestParam String userId) {
        try {
            ResponseEntity<String> response = postService.updatePost(id, postDTO, userId);
            return new ResponseEntity<>(response.getBody(), response.getStatusCode());
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        }
    }

    // 게시물 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePost(@PathVariable("id") Long id, @RequestParam String userId) {
        try {
            ResponseEntity<String> response = postService.deletePost(id, userId);
            return new ResponseEntity<>(response.getBody(), response.getStatusCode());
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        }
    }

    // 게시물 하트 누르기
    @PostMapping("/{postId}/heart")
    public ResponseEntity<String> togglePostHeart(@PathVariable Long postId, @RequestParam String userId) {
        postService.togglePostHeart(userId, postId);
        return ResponseEntity.ok("Heart toggled successfully");
    }


    // 게시물 하트 누른 사람 조회
    @GetMapping("/{id}/hearts/users")
    public PostHeartUsersResponse getPostHeartUsers(@PathVariable Long id) {
        return postService.getPostHeartUsers(id);
    }
}
