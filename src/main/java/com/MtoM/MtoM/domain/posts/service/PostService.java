package com.MtoM.MtoM.domain.posts.service;

import com.MtoM.MtoM.domain.posts.dao.CommentResponse;
import com.MtoM.MtoM.domain.posts.dao.PostHeartUsersResponse;
import com.MtoM.MtoM.domain.posts.dao.PostResponse;
import com.MtoM.MtoM.domain.posts.dao.PostUserResponse;
import com.MtoM.MtoM.domain.posts.domain.PostCommentDomain;
import com.MtoM.MtoM.domain.posts.domain.PostDomain;
import com.MtoM.MtoM.domain.posts.dto.CreatePostDTO;
import com.MtoM.MtoM.domain.posts.dto.UpdatePostDTO;
import com.MtoM.MtoM.domain.posts.repository.PostCommentRepository;
import com.MtoM.MtoM.domain.posts.repository.PostRepository;
import com.MtoM.MtoM.domain.user.domain.UserDomain;
import com.MtoM.MtoM.domain.user.repository.UserRepository;
import com.MtoM.MtoM.global.S3Service.S3Service;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final PostRedisService redisService;
    private final PostCommentRedisService postCommentRedisService;
    private final UserRepository userRepository;
    private final PostCommentRepository postCommentRepository;
    private final S3Service s3Service;
    private RedisTemplate<String, Integer> redisTemplate;

    private static final String VIEW_COUNT_KEY_PREFIX = "post:count:";

    @Scheduled(cron = "0 0 0 * * *") // 매일 자정에 실행
    @Transactional
    public void updateViewCounts(){
        List<PostDomain> allPorts = postRepository.findAll();
        for(PostDomain post : allPorts) {
            String key = VIEW_COUNT_KEY_PREFIX + post.getId();
            Integer viewCount = redisTemplate.opsForValue().get(key);

            if(viewCount != null && viewCount > 0) {
                post.setViews(post.getViews() + viewCount); // 데이터베이승 view 업데이트
                postRepository.save(post); // 저장
                redisTemplate.delete(key); // Redis의 값 초기화
            }
        }
    }


    // 모든 게시물 조회
    public List<Map<String, Object>> getAllPosts() {
        List<PostDomain> posts = postRepository.findAll();

        return posts.stream().map(post -> {
            Map<String, Object> postMap = new HashMap<>();
            postMap.put("post", post);
            postMap.put("hearts", redisService.getPostHearts((post.getId())));
            postMap.put("views", redisService.getViewCount(post.getId()));
            return postMap;
        }).collect(Collectors.toList());
    }

    // 게시물 상세 조회
    public PostDomain getPostById(Long postId) {
        PostDomain post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시물을 찾을 수 없습니다."));
        // 게시물 조회수 증가
        redisService.incrementViewCount(postId); // postId를 문자열로 변환하여 전달
        return post;
    }

    // 게시물 생성
    public ResponseEntity<String> createPost(CreatePostDTO postDTO) {
        // CreatePostDTO에서 필요한 정보를 사용하여 PostDomain 객체 생성
        PostDomain post = new PostDomain();
        post.setTitle(postDTO.getTitle());
        post.setContent(postDTO.getContent());
        post.setHashtags(postDTO.getHashtags());

        // userId를 이용하여 사용자를 검색하여 user 필드에 설정
        UserDomain user = userRepository.findById(postDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        post.setUser(user);

        // 이미지 파일이 있는 경우 S3에 업로드하고 URL을 설정
        MultipartFile imgFile = postDTO.getImg();
        if (imgFile != null && !imgFile.isEmpty()) {
            try {
                String imgUrl = s3Service.uploadImage(imgFile, "post");
                post.setImg(imgUrl);
            } catch (IOException e) {
                return new ResponseEntity<>("{\"message\": \"이미지 업로드 중 오류가 발생했습니다.\"}", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        // PostRepository를 통해 저장
        postRepository.save(post);

        // 저장된 메시지를 JSON 형식으로 반환
        String responseJson = "{"
                + "\"message\": \"게시물이 성공적으로 생성되었습니다.\""
                + "}";

        return new ResponseEntity<>(responseJson, HttpStatus.CREATED);
    }


    // 게시물 수정
    public ResponseEntity<String> updatePost(Long postId, UpdatePostDTO updatedPostDTO, String userId) {
        PostDomain existingPost = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시물을 찾을 수 없습니다."));
        if (!existingPost.getUser().getId().equals(userId)) {
            throw new RuntimeException("해당 사용자는 게시물을 수정할 권한이 없습니다.");
        }

        existingPost.setTitle(updatedPostDTO.getTitle());
        existingPost.setContent(updatedPostDTO.getContent());
        existingPost.setHashtags(updatedPostDTO.getHashtags());

        MultipartFile imgFile = updatedPostDTO.getImg();
        if (imgFile != null && !imgFile.isEmpty()) {
            try {
                if (existingPost.getImg() != null) {
                    s3Service.deleteImage(existingPost.getImg());
                }
                String imgUrl = s3Service.uploadImage(imgFile, "post");
                existingPost.setImg(imgUrl);
            } catch (IOException e) {
                return new ResponseEntity<>("{\"message\": \"이미지 업로드 중 오류가 발생했습니다.\"}", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        postRepository.save(existingPost);

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

        if (existingPost.getImg() != null) {
            s3Service.deleteImage(existingPost.getImg());
        }

        postRepository.deleteById(postId);

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

    // 게시물 상세
    public PostResponse getPostDetail(Long id) {
        PostDomain post = postRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid post ID"));

        // 게시물 조회수 증가
        redisService.incrementViewCount(id);

        PostUserResponse userResponse = new PostUserResponse();
        UserDomain user = post.getUser();
        String profileImgURL = s3Service.getImagePath(user.getId());

        userResponse.setProfile(profileImgURL);
        userResponse.setName(user.getStudent_id() + " " + user.getName());
        userResponse.setMajor(user.getMajor().toString());
        userResponse.setUserId(user.getId());

        PostResponse response = new PostResponse();
        response.setPostId(post.getId());
        response.setTitle(post.getTitle());
        response.setContent(post.getContent());
        response.setImg(post.getImg());
        response.setHashtags(post.getHashtags());
        response.setCommentCount(postCommentRepository.countByPostId(id)); // 댓글 수를 따로 계산
        response.setHeartCount(redisService.getPostHearts(post.getId()));
        response.setView(redisService.getViewCount(post.getId()));
        response.setCreatedAt(formatDate(post.getCreatedAt()));
        response.setUser(List.of(userResponse));

        return response;
    }


    public List<CommentResponse> getPostComments(Long postId) {
        List<PostCommentDomain> comments = postCommentRepository.findByPostId(postId);
        return comments.stream().map(this::convertToCommentResponse).collect(Collectors.toList());
    }


    private CommentResponse convertToCommentResponse(PostCommentDomain comment) {
        CommentResponse response = new CommentResponse();
        response.setCommentId(comment.getId());
        response.setContent(comment.getContent());
        response.setTime(formatTimeAgo(comment.getCreatedAt()));
        response.setHeartCount(postCommentRedisService.getPostCommentHearts(comment.getId()));

        UserDomain user = comment.getUser();
        // TODO : null 일때 기본 프로필 되도록 수정하기
        String profileImgURL = s3Service.getImagePath(user.getId());
        response.setUserId(user.getId());
        response.setProfile(profileImgURL);
        response.setName(user.getStudent_id() + " " + user.getName());
        return response;
    }

    public PostHeartUsersResponse getPostHeartUsers(Long id) {
        PostDomain post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid post ID"));

        PostHeartUsersResponse response = new PostHeartUsersResponse();
        response.setHeartCount(redisService.getPostHearts(post.getId()));
        response.setCommentCount(postCommentRepository.countByPostId(id));
        Set<String> userIds = redisService.getPostHeartedUsers(id);

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



    private String formatDate(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
        return dateTime.format(formatter);
    }

    private String formatTimeAgo(LocalDateTime createdAt) {
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(createdAt, now);

        if (duration.toMinutes() < 1) {
            return "방금 전";
        } else if (duration.toMinutes() < 60) {
            return duration.toMinutes() + "분 전";
        } else if (duration.toHours() < 24) {
            return duration.toHours() + "시간 전";
        } else if (duration.toDays() < 2) {
            return "하루 전";
        } else if (duration.toDays() < 7) {
            return duration.toDays() + "일 전";
        } else if (duration.toDays() < 30) {
            return duration.toDays() / 7 + "주 전";
        } else if (duration.toDays() < 365) {
            return duration.toDays() / 30 + "달 전";
        } else {
            return duration.toDays() / 365 + "년 전";
        }
    }
}
