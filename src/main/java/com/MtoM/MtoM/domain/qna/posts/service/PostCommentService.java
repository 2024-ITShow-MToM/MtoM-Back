package com.MtoM.MtoM.domain.qna.posts.service;

import com.MtoM.MtoM.domain.qna.posts.domain.PostCommentDomain;
import com.MtoM.MtoM.domain.qna.posts.dto.CreatePostComment;
import com.MtoM.MtoM.domain.qna.posts.dto.UpdatePostComment;
import com.MtoM.MtoM.domain.qna.posts.repository.PostCommentRepository;
import com.MtoM.MtoM.domain.qna.posts.domain.PostDomain;
import com.MtoM.MtoM.domain.qna.posts.repository.PostRepository;
import com.MtoM.MtoM.domain.user.domain.UserDomain;
import com.MtoM.MtoM.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostCommentService {

    private final PostCommentRepository postCommentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;


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

}