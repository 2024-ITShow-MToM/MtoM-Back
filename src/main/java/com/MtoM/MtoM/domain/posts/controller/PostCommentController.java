package com.MtoM.MtoM.domain.posts.controller;

import com.MtoM.MtoM.domain.posts.dto.CreatePostComment;
import com.MtoM.MtoM.domain.posts.dto.UpdatePostComment;
import com.MtoM.MtoM.domain.posts.dao.PostHeartUsersResponse;
import com.MtoM.MtoM.domain.posts.service.PostCommentService;
import com.MtoM.MtoM.global.message.ResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts/comments")
@RequiredArgsConstructor
public class PostCommentController {

    private final PostCommentService postCommentService;

    // 댓글 생성 API
    @PostMapping
    public ResponseEntity<ResponseMessage> createComment(@RequestBody CreatePostComment createPostComment) {
        postCommentService.createComment(createPostComment);
        return ResponseEntity.ok(new ResponseMessage("댓글이 성공적으로 생성되었습니다."));
    }

    // 댓글 수정 API
    @PatchMapping("/{commentId}")
    public ResponseEntity<ResponseMessage> updateComment(
            @PathVariable Long commentId,
            @RequestParam String userId,
            @RequestBody UpdatePostComment updateDTO) {

        boolean isUpdated = postCommentService.updateComment(userId, commentId, updateDTO);
        if (isUpdated) {
            return ResponseEntity.ok(new ResponseMessage("댓글이 성공적으로 수정되었습니다."));
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ResponseMessage("댓글 수정 권한이 없습니다."));
        }
    }

    // 댓글 삭제 API
    @DeleteMapping("/{commentId}")
    public ResponseEntity<ResponseMessage> deleteComment(@RequestParam String userId, @PathVariable Long commentId) {
        if (postCommentService.deleteComment(userId, commentId)) {
            return ResponseEntity.ok(new ResponseMessage("댓글이 성공적으로 삭제되었습니다."));
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ResponseMessage("댓글 삭제 권한이 없습니다."));
        }
    }

    @PostMapping("/{commentId}/heart")
    public ResponseEntity<ResponseMessage> togglePostCommentHeart(@PathVariable Long commentId, @RequestParam String userId) {
        postCommentService.togglePostCommentHeart(userId, commentId);
        return ResponseEntity.ok(new ResponseMessage("Heart toggled successfully."));
    }

    // 게시물 댓글 하트 누른 사람 조회
    @GetMapping("/{id}/hearts/users")
    public PostHeartUsersResponse getPostHeartUsers(@PathVariable Long id) {
        return postCommentService.getPostHeartUsers(id);
    }
}