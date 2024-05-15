package com.MtoM.MtoM.domain.qna.post.service;

import com.MtoM.MtoM.domain.qna.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PostService {
    private final PostRepository postRepository;
}
