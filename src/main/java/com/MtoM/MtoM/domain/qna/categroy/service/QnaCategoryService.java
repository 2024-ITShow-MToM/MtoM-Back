package com.MtoM.MtoM.domain.qna.categroy.service;

import com.MtoM.MtoM.domain.qna.categroy.dao.QnaPostResponse;
import com.MtoM.MtoM.domain.qna.categroy.dao.QnaSelectResponse;
import com.MtoM.MtoM.domain.qna.categroy.dao.VoteOptionResponse;
import com.MtoM.MtoM.domain.qna.posts.dao.CommentResponse;
import com.MtoM.MtoM.domain.qna.posts.domain.PostCommentDomain;
import com.MtoM.MtoM.domain.qna.posts.domain.PostDomain;
import com.MtoM.MtoM.domain.qna.posts.repository.PostCommentRepository;
import com.MtoM.MtoM.domain.qna.posts.repository.PostRepository;
import com.MtoM.MtoM.domain.qna.posts.service.PostCommentRedisService;
import com.MtoM.MtoM.domain.qna.posts.service.PostRedisService;
import com.MtoM.MtoM.domain.qna.selects.domain.SelectDomain;
import com.MtoM.MtoM.domain.qna.selects.repository.SelectRepository;
import com.MtoM.MtoM.domain.qna.selects.service.VoteService;
import com.MtoM.MtoM.domain.user.domain.UserDomain;
import com.MtoM.MtoM.domain.user.repository.UserRepository;
import com.MtoM.MtoM.global.S3Service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class QnaCategoryService {

    @Autowired
    private final PostRepository postRepository;

    @Autowired
    private final SelectRepository selectRepository;

    @Autowired
    private final PostRedisService redisService;

    @Autowired
    private final VoteService voteService;

    @Autowired
    private final PostCommentRedisService postCommentRedisService;

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final PostCommentRepository postCommentRepository;
    @Autowired
    private final S3Service s3Service;
    @Autowired
    private RedisTemplate<String, Integer> redisTemplate;
    @Autowired
    private RedisTemplate<String, String> stringRedisTemplate;


    private static final String VIEW_COUNT_KEY_PREFIX = "post:count:";
    private static final String VOTE_COUNT_PREFIX = "votes:count:";

    public QnaCategoryService(PostRepository postRepository, SelectRepository selectRepository, PostRedisService redisService, VoteService voteService, PostCommentRedisService postCommentRedisService, UserRepository userRepository, PostCommentRepository postCommentRepository, S3Service s3Service) {
        this.postRepository = postRepository;
        this.selectRepository = selectRepository;
        this.redisService = redisService;
        this.voteService = voteService;
        this.postCommentRedisService = postCommentRedisService;
        this.userRepository = userRepository;
        this.postCommentRepository = postCommentRepository;
        this.s3Service = s3Service;
    }
    public List<QnaPostResponse> getQnaPostsSortedByComments() {
        List<PostDomain> posts = postRepository.findAll();

        return posts.stream()
                .map(post -> createQnaPostResponse(post))
                .sorted(Comparator.comparing(QnaPostResponse::getCommentCount).reversed())
                .collect(Collectors.toList());
    }

    public List<QnaPostResponse> getQnaPostsSortedByHearts() {
        List<PostDomain> posts = postRepository.findAll();

        return posts.stream()
                .map(post -> createQnaPostResponse(post))
                .sorted(Comparator.comparing(QnaPostResponse::getHeartCount).reversed())
                .collect(Collectors.toList());
    }

    public List<QnaPostResponse> getQnaPostsSortedByViews() {
        List<PostDomain> posts = postRepository.findAll();

        return posts.stream()
                .map(post -> createQnaPostResponse(post))
                .sorted(Comparator.comparing(QnaPostResponse::getViewCount).reversed())
                .collect(Collectors.toList());
    }

    private QnaPostResponse createQnaPostResponse(PostDomain post) {
        List<PostCommentDomain> comments = postCommentRepository.findByPostId(post.getId());
        QnaPostResponse response = new QnaPostResponse();
        response.setPostId(post.getId());
        response.setTitle(post.getTitle());
        response.setImg(post.getImg());
        response.setHashtags(post.getHashtags());
        response.setCommentCount(comments.size());
        response.setHeartCount(redisService.getPostHearts(post.getId()));
        response.setViewCount(redisService.getViewCount(post.getId()));
        response.setCreatedAt(formatDate(post.getCreatedAt()));
        return response;
    }

    // 게시물 리스트
    public List<QnaPostResponse> getQnaPosts() {
        List<PostDomain> posts = postRepository.findAll();


        return posts.stream().map(post -> {
            List<PostCommentDomain> comments = postCommentRepository.findByPostId(post.getId());
            QnaPostResponse response = new QnaPostResponse();
            response.setPostId(post.getId());
            response.setTitle(post.getTitle());
            response.setImg(post.getImg());
            response.setHashtags(post.getHashtags());
            response.setCommentCount(comments.size());
            response.setHeartCount(redisService.getPostHearts(post.getId()));
            response.setViewCount(redisService.getViewCount(post.getId()));
            response.setCreatedAt(formatDate(post.getCreatedAt()));
            return response;
        }).collect(Collectors.toList());
    }
//    public List<QnaSelectResponse> getQnaSelects() {
//        List<SelectDomain> selects = selectRepository.findAll();
//
//        return selects.stream().map(select -> {
//            QnaSelectResponse response = new QnaSelectResponse();
//            response.setSelectId(select.getId());
//            response.setTitle(select.getTitle());
//            response.setParticipants(getVotePercentage(select.getId()).getParticipants());
//            response.setOptions(getVotePercentage(select.getId()).getOptions());
//            response.setDate(formatDate(select.getCreatedAt()));
//            return response;
//        }).collect(Collectors.toList());
//    }

    private String formatDate(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
        return dateTime.format(formatter);
    }


    public QnaSelectResponse getVotePercentage(Long selectId, String userId) {
        String voteCountKey = VOTE_COUNT_PREFIX + selectId;

        Optional<SelectDomain> selectDomainOpt = selectRepository.findById(selectId);
        if (!selectDomainOpt.isPresent()) {
            // selectId에 해당하는 데이터가 없는 경우 처리
            throw new RuntimeException("SelectDomain not found for id: " + selectId);
        }

        SelectDomain selectDomain = selectDomainOpt.get();

        Map<Object, Object> voteCounts = stringRedisTemplate.opsForHash().entries(voteCountKey);

        long totalVotes = voteCounts.values().stream()
                .mapToLong(value -> Long.parseLong(value.toString()))
                .sum();

        List<VoteOptionResponse> options = new ArrayList<>();
        String option1 = selectDomain.getOption1();
        String option2 = selectDomain.getOption2();
        if (voteCounts.size() == 2) {
            List<Object> optionKeys = new ArrayList<>(voteCounts.keySet());
            String option1Key = optionKeys.get(0).toString();
            String option2Key = optionKeys.get(1).toString();

            long option1Count = Long.parseLong(voteCounts.get(option1Key).toString());
            long option2Count = Long.parseLong(voteCounts.get(option2Key).toString());

            int percentage1 = (int) ((option1Count * 100) / totalVotes);
            int percentage2 = (int) ((option2Count * 100) / totalVotes);

            VoteOptionResponse response = new VoteOptionResponse();
            response.setOption1(option1);
            response.setPercentage1(percentage1);
            response.setOption2(option2);
            response.setPercentage2(percentage2);

            options.add(response);
        }

        QnaSelectResponse response = new QnaSelectResponse();
        response.setSelectId(selectId);
        response.setOptions(options);
        response.setParticipants(totalVotes);

        return response;
    }
}
