package com.MtoM.MtoM.domain.qna.categroy.service;

import com.MtoM.MtoM.domain.qna.categroy.dao.QnaPostResponse;
import com.MtoM.MtoM.domain.qna.categroy.dao.QnaSelectResponse;
import com.MtoM.MtoM.domain.qna.categroy.dao.VoteOptionResponse;
import com.MtoM.MtoM.domain.qna.posts.domain.PostCommentDomain;
import com.MtoM.MtoM.domain.qna.posts.domain.PostDomain;
import com.MtoM.MtoM.domain.qna.posts.repository.PostCommentRepository;
import com.MtoM.MtoM.domain.qna.posts.repository.PostRepository;
import com.MtoM.MtoM.domain.qna.posts.service.PostCommentRedisService;
import com.MtoM.MtoM.domain.qna.posts.service.PostRedisService;
import com.MtoM.MtoM.domain.qna.selects.domain.SelectDomain;
import com.MtoM.MtoM.domain.qna.selects.repository.SelectRepository;
import com.MtoM.MtoM.domain.qna.selects.service.VoteService;
import com.MtoM.MtoM.domain.user.repository.UserRepository;
import com.MtoM.MtoM.global.S3Service.S3Service;
import lombok.extern.slf4j.Slf4j;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

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

    public List<QnaPostResponse> getQnaPostsSortedByCreatedAt() {
        List<PostDomain> posts = postRepository.findAll();


        return posts.stream()
                .map(post -> createQnaPostResponse(post))
                .sorted(Comparator.comparing(QnaPostResponse::getCreatedAt).reversed())
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

    private String formatDate(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
        return dateTime.format(formatter);
    }


    public List<QnaSelectResponse> getAllQnaSelectResponses(String userId) {
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
                try {
                    totalVotes += Double.parseDouble(count.toString());
                } catch (NumberFormatException e) {
                    log.error("Invalid number format for vote count: " + count, e);
                }
            }

            // 각 옵션의 퍼센트 계산
            String option1Content = selectDomain.getOption1();
            String option2Content = selectDomain.getOption2();

            // 첫 번째 옵션의 퍼센트 계산
            double countOption1 = 0;
            try {
                countOption1 = Double.parseDouble(voteCounts.getOrDefault("option1", "0").toString());
            } catch (NumberFormatException e) {
                log.error("Invalid number format for option1 count: " + voteCounts.get("option1"), e);
            }
            double percentageOption1 = (totalVotes == 0) ? 0 : (countOption1 / totalVotes) * 100;

            // 두 번째 옵션의 퍼센트 계산
            double countOption2 = 0;
            try {
                countOption2 = Double.parseDouble(voteCounts.getOrDefault("option2", "0").toString());
            } catch (NumberFormatException e) {
                log.error("Invalid number format for option2 count: " + voteCounts.get("option2"), e);
            }
            double percentageOption2 = (totalVotes == 0) ? 0 : (countOption2 / totalVotes) * 100;

            // VoteOptionResponse 객체 생성
            VoteOptionResponse voteOptionResponse = new VoteOptionResponse();
            voteOptionResponse.setOption1(option1Content);
            voteOptionResponse.setPercentage1(percentageOption1);
            voteOptionResponse.setOption2(option2Content);
            voteOptionResponse.setPercentage2(percentageOption2);

            // 사용자가 선택한 옵션 가져오기
            String userVoteKey = USER_VOTES_PREFIX + userId;
            String userSelectedOption = (String) stringRedisTemplate.opsForHash().get(userVoteKey, selectId.toString());

            QnaSelectResponse qnaSelectResponse = new QnaSelectResponse();
            qnaSelectResponse.setSelectId(selectId);
            qnaSelectResponse.setTitle(selectDomain.getTitle());
            qnaSelectResponse.setCreatedAt(formatDate(selectDomain.getCreatedAt()));
            qnaSelectResponse.setParticipants((int) totalVotes);
            qnaSelectResponse.setUserSelect(userSelectedOption); // user가 선택한 옵션을 설정
            qnaSelectResponse.setOptions(List.of(voteOptionResponse));

            qnaSelectResponses.add(qnaSelectResponse);
        }

        // 최신순으로 정렬
        qnaSelectResponses.sort(Comparator.comparing(QnaSelectResponse::getCreatedAt).reversed());

        return qnaSelectResponses;
    }

    public List<Object> getQnaPostsAndSelectsSortedByCreatedAt(String userId, String keyword) {
        List<QnaPostResponse> postResponses = getQnaPosts();
        List<QnaSelectResponse> selectResponses = getAllQnaSelectResponses(userId);

        // Post와 Select 응답을 합친 후, createdAt을 기준으로 최신순으로 정렬
        List<Object> combinedResponses = new ArrayList<>();
        combinedResponses.addAll(postResponses);
        combinedResponses.addAll(selectResponses);

        // 정렬
        combinedResponses = combinedResponses.stream()
                .filter(response -> filterByKeyword(response, keyword))
                .sorted((o1, o2) -> {
                    LocalDateTime createdAt1 = getCreatedAt(o1);
                    LocalDateTime createdAt2 = getCreatedAt(o2);

                    if (createdAt1 != null && createdAt2 != null) {
                        return -1 * createdAt1.compareTo(createdAt2);
                    } else if (createdAt1 != null) {
                        return -1;
                    } else if (createdAt2 != null) {
                        return 1;
                    } else {
                        return 0;
                    }
                })
                .collect(Collectors.toList());

        return combinedResponses;
    }

    private boolean filterByKeyword(Object obj, String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            return true;
        }
        if (obj instanceof QnaPostResponse) {
            QnaPostResponse post = (QnaPostResponse) obj;
            return post.getTitle().contains(keyword) ||
                    post.getHashtags().contains(keyword);
        } else if (obj instanceof QnaSelectResponse) {
            QnaSelectResponse select = (QnaSelectResponse) obj;
            return select.getTitle().contains(keyword);
        }
        return false;
    }

    // 객체에서 createdAt을 추출하는 메소드
    private LocalDateTime getCreatedAt(Object obj) {
        if (obj instanceof QnaPostResponse) {
            String createdAtString = ((QnaPostResponse) obj).getCreatedAt();
            return parseLocalDateTime(createdAtString);
        } else if (obj instanceof QnaSelectResponse) {
            String createdAtString = ((QnaSelectResponse) obj).getCreatedAt();
            return parseLocalDateTime(createdAtString);
        }
        return null;
    }

    // createdAt 문자열을 LocalDateTime으로 파싱하는 메소드
    private LocalDateTime parseLocalDateTime(String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
        java.time.LocalDate localDate = LocalDate.parse(dateString, formatter);
        return localDate.atStartOfDay();
    }



}
