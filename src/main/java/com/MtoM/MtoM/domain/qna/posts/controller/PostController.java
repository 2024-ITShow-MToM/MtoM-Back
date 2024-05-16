package com.MtoM.MtoM.domain.qna.posts.controller;

import com.MtoM.MtoM.domain.qna.posts.domain.PostDomain;
import com.MtoM.MtoM.domain.qna.posts.dto.CreatePostDTO;
import com.MtoM.MtoM.domain.qna.posts.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

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

    // 게시물 생성
    @PostMapping
    public ResponseEntity<String> createPost(@RequestBody CreatePostDTO postDTO) {
        ResponseEntity<String> response = postService.createPost(postDTO);
        return new ResponseEntity<>(response.getBody(), response.getStatusCode());
    }


    // 게시물 수정
    @PatchMapping("/{id}")
    public ResponseEntity<String> updatePost(@PathVariable("id") Long id, @RequestBody PostDomain post, @RequestParam String userId) {
        try {
            ResponseEntity<String> response = postService.updatePost(id, post, userId);
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
    @PostMapping("/{postId}/heart")
    public ResponseEntity<String> togglePostHeart(@PathVariable Long postId, @RequestParam String userId) {
        postService.togglePostHeart(userId, postId);
        return ResponseEntity.ok("Heart toggled successfully");
    }
}
