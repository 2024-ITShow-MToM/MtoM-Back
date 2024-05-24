package com.MtoM.MtoM.domain.qna.selects.service;

import com.MtoM.MtoM.domain.qna.categroy.dao.QnaSelectResponse;
import com.MtoM.MtoM.domain.qna.categroy.dao.VoteOptionResponse;
import com.MtoM.MtoM.domain.qna.selects.domain.SelectDomain;
import com.MtoM.MtoM.domain.qna.selects.repository.SelectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class VoteService {
    private final RedisTemplate<String, String> redisTemplate;
    private static final String VOTE_COUNT_PREFIX = "votes:count:";
    private static final String VOTE_USERS_PREFIX = "votes:users:";
    private static final String USER_VOTES_PREFIX = "user:votes:";
    @Autowired
    private final SelectRepository selectRepository;

    public VoteService(RedisTemplate<String, String> redisTemplate, SelectRepository selectRepository) {
        this.redisTemplate = redisTemplate;
        this.selectRepository = selectRepository;
    }

    public void vote(Long selectId, String option, String userId) {
        String voteCountKey = VOTE_COUNT_PREFIX + selectId;
        String voteUsersKey = VOTE_USERS_PREFIX + selectId;
        String userVoteKey = USER_VOTES_PREFIX + userId;

        // 사용자가 이미 투표했는지 확인
        String previousVote = (String) redisTemplate.opsForHash().get(userVoteKey, selectId.toString());

        if (previousVote != null) {
            // 사용자가 이미 투표한 경우, 이전 투표 수 감소
            redisTemplate.opsForHash().increment(voteCountKey, previousVote, -1);
        }

        // 새로운 투표 수 증가
        redisTemplate.opsForHash().increment(voteCountKey, option, 1);
        // 사용자가 새로운 옵션에 투표한 것으로 기록
        redisTemplate.opsForSet().add(voteUsersKey, userId);
        // 사용자의 투표 기록 업데이트
        redisTemplate.opsForHash().put(userVoteKey, selectId.toString(), option);
    }

    public Map<Object, Object> getVoteResult(Long selectId) {
        String voteCountKey = VOTE_COUNT_PREFIX + selectId;
        return redisTemplate.opsForHash().entries(voteCountKey);
    }

    public Map<String, Double> getVotePercentages(Long selectId) {
        LinkedHashMap<String, Double> percentages = new LinkedHashMap<>();

        String voteCountKey = VOTE_COUNT_PREFIX + selectId;
        Map<Object, Object> voteCounts = redisTemplate.opsForHash().entries(voteCountKey);

        // 전체 투표 수 계산
        double totalVotes = 0;
        for (Object count : voteCounts.values()) {
            totalVotes += Double.parseDouble(count.toString());
        }

        Optional<SelectDomain> selectDomainOpt = selectRepository.findById(selectId);
        if (!selectDomainOpt.isPresent()) {
            // selectId에 해당하는 데이터가 없는 경우 처리
            throw new RuntimeException("SelectDomain not found for id: " + selectId);
        }

        // 선택지 도메인 객체
        SelectDomain selectDomain = selectDomainOpt.get();

        // 각 옵션의 퍼센트 계산
        String option1Content = selectDomain.getOption1();
        String option2Content = selectDomain.getOption2();

        // 첫 번째 옵션의 퍼센트 계산
        double countOption1 = Double.parseDouble(voteCounts.getOrDefault("option1", "0").toString());
        double percentageOption1 = (countOption1 / totalVotes) * 100;
        percentages.put(option1Content, percentageOption1);

        // 두 번째 옵션의 퍼센트 계산
        double countOption2 = Double.parseDouble(voteCounts.getOrDefault("option2", "0").toString());
        double percentageOption2 = (countOption2 / totalVotes) * 100;
        percentages.put(option2Content, percentageOption2);

        return percentages;
    }
}
