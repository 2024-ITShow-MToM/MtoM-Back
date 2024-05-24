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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
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
    private static final String USER_VOTES_PREFIX = "user:votes:";

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


    public List<QnaSelectResponse> getAllQnaSelectResponses() {
        List<SelectDomain> allSelectDomains = selectRepository.findAll();
        List<QnaSelectResponse> qnaSelectResponses = new ArrayList<>();

        for (SelectDomain selectDomain : allSelectDomains) {
            // 각 선택지에 대한 로직을 반복
            Long selectId = selectDomain.getId();
            log.info("selectId : " + selectId);
            // Redis에서 투표 데이터를 가져옴
            String voteCountKey = VOTE_COUNT_PREFIX + selectId;
            Map<Object, Object> voteCounts = redisTemplate.opsForHash().entries(voteCountKey);

            // 전체 투표 수 계산
            double totalVotes = 0;
            for (Object count : voteCounts.values()) {
                totalVotes += Double.parseDouble(count.toString());
            }

            // 각 옵션의 퍼센트 계산
            String option1Content = selectDomain.getOption1();
            String option2Content = selectDomain.getOption2();

            // 첫 번째 옵션의 퍼센트 계산
            double countOption1 = Double.parseDouble(voteCounts.getOrDefault("option1", "0").toString());
            double percentageOption1 = (totalVotes == 0) ? 0 : (countOption1 / totalVotes) * 100;

            // 두 번째 옵션의 퍼센트 계산
            double countOption2 = Double.parseDouble(voteCounts.getOrDefault("option2", "0").toString());
            double percentageOption2 = (totalVotes == 0) ? 0 : (countOption2 / totalVotes) * 100;

            // VoteOptionResponse 객체 생성
            VoteOptionResponse voteOptionResponse = new VoteOptionResponse();
            voteOptionResponse.setOption1(option1Content);
            voteOptionResponse.setPercentage1(percentageOption1);
            voteOptionResponse.setOption2(option2Content);
            voteOptionResponse.setPercentage2(percentageOption2);

            QnaSelectResponse qnaSelectResponse = new QnaSelectResponse();
            qnaSelectResponse.setSelectId(selectId);
            qnaSelectResponse.setTitle(selectDomain.getTitle());
            qnaSelectResponse.setCreatedAt(selectDomain.getCreatedAt().toString());
            qnaSelectResponse.setParticipants((int) totalVotes);
            // TODO : userId를 받은 user가 선택한 옵션을 String으로 추가하기
            qnaSelectResponse.setUserSelect(null); // user가 선택한 옵션을 String으로 주기
            qnaSelectResponse.setOptions(List.of(voteOptionResponse));

            qnaSelectResponses.add(qnaSelectResponse);
        }

        return qnaSelectResponses;
    }

}
