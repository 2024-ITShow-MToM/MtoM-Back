package com.MtoM.MtoM.domain.qna.posts.service;

import com.MtoM.MtoM.domain.qna.posts.domain.PostCommentDomain;
import com.MtoM.MtoM.domain.qna.posts.dto.CreatePostComment;
import com.MtoM.MtoM.domain.qna.posts.dto.UpdatePostComment;
import com.MtoM.MtoM.domain.qna.posts.repository.PostCommentRepository;
import com.MtoM.MtoM.domain.qna.posts.domain.PostDomain;
import com.MtoM.MtoM.domain.qna.posts.repository.PostRepository;
import com.MtoM.MtoM.domain.user.domain.UserDomain;
import com.MtoM.MtoM.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostCommentService {

    private final PostCommentRepository postCommentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final PostCommentRedisService postCommentRedisService;

    private static final String HEART_COUNT_KEY_PREFIX = "post:comment:heart";
    private final RedisTemplate<String,Integer> redisTemplate;

    @Scheduled(cron = "0 0 0 * * *") //매일 자정에 실행
    @Transactional
    public void updateHeartCounts() {
        List<PostCommentDomain> allComments = postCommentRepository.findAll();
        for(PostCommentDomain comment : allComments) {
            String key = HEART_COUNT_KEY_PREFIX + comment.getId();
            Integer heartCount = redisTemplate.opsForValue().get(key);

            if(heartCount != null && heartCount > 0) {
                comment.setHearts(comment.getHearts());
                postCommentRepository.save(comment);
                redisTemplate.delete(key); // Redis값 초기화
            }
        }
    }

    public void createComment(CreatePostComment commentDTO) {
        PostCommentDomain postComment = new PostCommentDomain();
        postComment.setContent(commentDTO.getContent());

        // postId를 이용하여 게시물 검색 후 post 필드에 설정
        PostDomain post = postRepository.findById(commentDTO.getPostId())
                        .orElseThrow(() -> new RuntimeException("게시물을 찾을 수 없습니다."));
        postComment.setPost(post);

        UserDomain user = userRepository.findById(commentDTO.getUserId())
                        .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        postComment.setUser(user);

        postCommentRepository.save(postComment);
    }

    public boolean updateComment(String userId, Long commentId, UpdatePostComment updateDTO) {
        Optional<PostCommentDomain> commentOptional = postCommentRepository.findById(commentId);
        if (commentOptional.isPresent()) {
            PostCommentDomain comment = commentOptional.get();
            // userId를 사용하여 댓글 작성자 확인
            if (userId.equals(comment.getUser().getId())) {
                // 업데이트할 필드를 선택적으로 업데이트
                if (updateDTO.getContent() != null) {
                    comment.setContent(updateDTO.getContent());
                }
                postCommentRepository.save(comment);
                return true;
            }
        }
        return false;
    }

    public boolean deleteComment(String userId, Long commentId) {
        Optional<PostCommentDomain> commentOptional = postCommentRepository.findById(commentId);
        if (commentOptional.isPresent()) {
            PostCommentDomain comment = commentOptional.get();
            // userId를 사용하여 댓글 작성자 확인
            if (userId.equals(comment.getUser().getId())) {
                postCommentRepository.delete(comment);
                return true;
            }
        }
        return false;
    }

    public void togglePostCommentHeart(String userId, Long commentId) {
        // 게시물 댓글 하트 토글
        postCommentRedisService.togglePostCommentHeart(userId, commentId);
    }

    public void getPostCommentHeartCount(Long commentId) {
        // 게시물 댓글 하트 토글
        postCommentRedisService.getPostCommentHearts(commentId);
    }

}