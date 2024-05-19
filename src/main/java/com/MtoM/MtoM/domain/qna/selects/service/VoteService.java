package com.MtoM.MtoM.domain.qna.selects.service;

import com.MtoM.MtoM.domain.qna.categroy.dao.QnaSelectResponse;
import com.MtoM.MtoM.domain.qna.categroy.dao.VoteOptionResponse;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class VoteService {
    private final RedisTemplate<String, String> redisTemplate;
    private static final String VOTE_COUNT_PREFIX = "votes:count:";
    private static final String VOTE_USERS_PREFIX = "votes:users:";
    private static final String USER_VOTES_PREFIX = "user:votes:";

    public VoteService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
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
        Map<String, Double> percentages = new HashMap<>();

        String voteCountKey = VOTE_COUNT_PREFIX + selectId;
        Map<Object, Object> voteCounts = redisTemplate.opsForHash().entries(voteCountKey);

        // 전체 투표 수 계산
        double totalVotes = 0;
        for (Object count : voteCounts.values()) {
            totalVotes += Double.parseDouble(count.toString());
        }

        // 각 옵션의 퍼센트 계산
        for (Map.Entry<Object, Object> entry : voteCounts.entrySet()) {
            String option = entry.getKey().toString();
            double count = Double.parseDouble(entry.getValue().toString());
            double percentage = (count / totalVotes) * 100;
            percentages.put(option, percentage);
        }

        return percentages;
    }


}