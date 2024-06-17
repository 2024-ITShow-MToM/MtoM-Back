package com.MtoM.MtoM.domain.user.service;

import com.MtoM.MtoM.domain.posts.domain.PostCommentDomain;
import com.MtoM.MtoM.domain.posts.domain.PostDomain;
import com.MtoM.MtoM.domain.posts.repository.PostCommentRepository;
import com.MtoM.MtoM.domain.posts.repository.PostRepository;
import com.MtoM.MtoM.domain.posts.service.PostRedisService;
import com.MtoM.MtoM.domain.qna.categroy.dao.QnaPostResponse;
import com.MtoM.MtoM.global.redis.RedisService;
import com.MtoM.MtoM.global.util.DateTimeUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class GetQnaPostByUserService {
    private final PostRepository postRepository;
    private final PostCommentRepository postCommentRepository;
    private final PostRedisService redisService;

    @Transactional
    public List<QnaPostResponse> execute(String userId) {
        List<PostDomain> posts = postRepository.findAllByUserId(userId);
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
            response.setCreatedAt(DateTimeUtils.formatDate(post.getCreatedAt()));
            return response;
        }).collect(Collectors.toList());
    }
}
