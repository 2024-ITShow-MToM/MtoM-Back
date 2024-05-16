package com.MtoM.MtoM.domain.qna.posts.service;

import com.MtoM.MtoM.domain.qna.posts.domain.PostDomain;
import com.MtoM.MtoM.domain.qna.posts.dto.CreatePostDTO;
import com.MtoM.MtoM.domain.qna.posts.repository.PostRepository;
import com.MtoM.MtoM.domain.user.domain.UserDomain;
import com.MtoM.MtoM.domain.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final PostRedisService redisService;
    private final UserRepository userRepository;

    @Autowired
    public PostService(PostRepository postRepository, PostRedisService redisService, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.redisService = redisService;
        this.userRepository = userRepository;
    }

    // 모든 게시물 조회
    public List<Map<String, Object>> getAllPosts() {
        List<PostDomain> posts = postRepository.findAll();

        return posts.stream().map(post -> {
            Map<String, Object> postMap = new HashMap<>();
            postMap.put("post", post);
            postMap.put("hearts", redisService.getPostHearts(String.valueOf(post.getId())));
            postMap.put("views", redisService.getPostViews(String.valueOf(post.getId())));
            return postMap;
        }).collect(Collectors.toList());
    }

    // 게시물 상세 조회
    public PostDomain getPostById(Long postId) {
        PostDomain post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시물을 찾을 수 없습니다."));
        // 게시물 조회수 증가
        redisService.incrementPostViews(String.valueOf(postId)); // postId를 문자열로 변환하여 전달
        return post;
    }

    // 게시물 생성
    public ResponseEntity<String> createPost(CreatePostDTO postDTO) {
        // CreatePostDTO에서 필요한 정보를 사용하여 PostDomain 객체 생성
        PostDomain post = new PostDomain();
        post.setTitle(postDTO.getTitle());
        post.setContent(postDTO.getContent());
        post.setImg(postDTO.getImg());
        post.setHashtags(postDTO.getHashtags());

        // userId를 이용하여 사용자를 검색하여 user 필드에 설정
        UserDomain user = userRepository.findById(postDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        post.setUser(user);

        // PostRepository를 통해 저장
        postRepository.save(post);

        // 저장된 메시지를 JSON 형식으로 반환
        String responseJson = "{"
                + "\"message\": \"게시물이 성공적으로 생성되었습니다.\""
                + "}";

        return new ResponseEntity<>(responseJson, HttpStatus.CREATED);
    }


    // 게시물 수정
    public ResponseEntity<String> updatePost(Long postId, PostDomain updatedPost, String userId) {
        PostDomain existingPost = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시물을 찾을 수 없습니다."));
        if (!existingPost.getUser().getId().equals(userId)) {
            throw new RuntimeException("해당 사용자는 게시물을 수정할 권한이 없습니다.");
        }
        existingPost.setTitle(updatedPost.getTitle());
        existingPost.setContent(updatedPost.getContent());
        existingPost.setImg(updatedPost.getImg());
        existingPost.setHashtags(updatedPost.getHashtags());
        postRepository.save(existingPost);

        // 수정 완료 메시지를 JSON 형식으로 반환
        String responseJson = "{"
                + "\"message\": \"게시물이 성공적으로 수정되었습니다.\""
                + "}";

        return new ResponseEntity<>(responseJson, HttpStatus.OK);
    }

    // 게시물 삭제
    public ResponseEntity<String> deletePost(Long postId, String userId) {
        PostDomain existingPost = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시물을 찾을 수 없습니다."));
        if (!existingPost.getUser().getId().equals(userId)) {
            throw new RuntimeException("해당 사용자는 게시물을 삭제할 권한이 없습니다.");
        }
        postRepository.deleteById(postId);

        // 삭제 완료 메시지를 JSON 형식으로 반환
        String responseJson = "{"
                + "\"message\": \"게시물이 성공적으로 삭제되었습니다.\""
                + "}";

        return new ResponseEntity<>(responseJson, HttpStatus.OK);
    }

    public void togglePostHeart(String userId, Long postId) { // userId를 문자열로 사용
        // 게시물 하트 토글
        redisService.togglePostHeart(userId, postId);
    }

    public boolean isPostHearted(String userId, Long postId) { // userId를 문자열로 사용
        // 게시물 하트 여부 확인
        return redisService.isPostHearted(userId, postId);
    }
}
