package com.MtoM.MtoM.domain.posts.service;

import com.MtoM.MtoM.domain.notify.domain.NotificationType;
import com.MtoM.MtoM.domain.notify.domain.Notify;
import com.MtoM.MtoM.domain.notify.service.NotifyService;
import com.MtoM.MtoM.domain.posts.dao.PostHeartUsersResponse;
import com.MtoM.MtoM.domain.posts.dao.PostUserResponse;
import com.MtoM.MtoM.domain.posts.domain.PostCommentDomain;
import com.MtoM.MtoM.domain.posts.domain.PostDomain;
import com.MtoM.MtoM.domain.posts.dto.CreatePostComment;
import com.MtoM.MtoM.domain.posts.dto.UpdatePostComment;
import com.MtoM.MtoM.domain.posts.repository.PostCommentRepository;
import com.MtoM.MtoM.domain.posts.repository.PostRepository;
import com.MtoM.MtoM.domain.user.domain.UserDomain;
import com.MtoM.MtoM.domain.user.repository.UserRepository;
import com.MtoM.MtoM.global.S3Service.S3Service;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Sinks;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PostCommentService {

    private final PostCommentRepository postCommentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final PostCommentRedisService postCommentRedisService;
    private final NotifyService notifyService;
    private final Sinks.Many<Notify> sink;
    private final S3Service s3Service;

    // Redis 키 접두사 상수 선언
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

        UserDomain commentUser = userRepository.findById(commentDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        postComment.setUser(commentUser);

        // 게시물 작성자에게 알림 보내기
        UserDomain postAuthor = post.getUser();

        Notify notify = new Notify();
        notify.setContent("Q&A 답변: " + commentDTO.getContent());
        notify.setReceiver(postAuthor); // 게시물 작성자를 알림의 수신자로 설정
        notify.setSender(commentUser); // 댓글 작성자를 알림의 발신자로 설정
        notify.setNotificationType(NotificationType.COMMENT); // 알림 타입 설정
        notify.setIsRead(false);
        notify.setContentId(commentDTO.getPostId());
        notifyService.saveNotification(notify);

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

    public PostHeartUsersResponse getPostHeartUsers(Long id) {
        PostCommentDomain post = postCommentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid comment ID"));

        PostHeartUsersResponse response = new PostHeartUsersResponse();
        response.setHeartCount(postCommentRedisService.getPostCommentHearts(post.getId()));
        response.setCommentCount(postCommentRepository.countByPostId(post.getId()));

        Set<String> userIds = postCommentRedisService.getPostHeartedUsers(id);

        List<PostUserResponse> userResponses = new ArrayList<>();
        for (String userIdStr : userIds) {
            String userId;
            try {
                userId = userIdStr;
            } catch (NumberFormatException e) {
                System.err.println("Invalid user ID format: " + userIdStr);
                continue;
            }

            try {
                UserDomain user = userRepository.findById(userId)
                        .orElseThrow(() -> new IllegalArgumentException("Invalid user ID: " + userId));

                String profileImgURL = s3Service.getImagePath(user.getId());
                PostUserResponse userResponse = new PostUserResponse();
                userResponse.setUserId(user.getId());
                userResponse.setProfile(profileImgURL);
                userResponse.setMajor(user.getMajor().toString());
                userResponse.setName(user.getStudent_id() + " " + user.getName());

                userResponses.add(userResponse);
            } catch (IllegalArgumentException e) {
                System.err.println("User not found for ID: " + userId);
            }
        }

        response.setUsers(userResponses);
        return response;
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