package com.MtoM.MtoM.domain.groupChat;

import com.MtoM.MtoM.domain.groupChat.domain.GroupChatContentDomain;
import com.MtoM.MtoM.domain.groupChat.dto.req.SendChatRequestDto;
import com.MtoM.MtoM.domain.groupChat.dto.res.GroupChatListResponseDto;
import com.MtoM.MtoM.domain.groupChat.repository.GroupChatContentRepository;
import com.MtoM.MtoM.domain.groupChat.service.GroupChartListService;
import com.MtoM.MtoM.domain.groupChat.service.SendGroupChatService;
import com.MtoM.MtoM.global.message.ResponsePayload;
import lombok.RequiredArgsConstructor;
import org.hibernate.query.sql.internal.ParameterRecognizerImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chats/groups")
public class GroupChatController {
    private final GroupChartListService groupChartList;
    private final SendGroupChatService sendGroupChatService;

    //나의 채팅 내역 불러오기
    @GetMapping("/list/{userId}")
    public ResponseEntity<ResponsePayload<List<GroupChatListResponseDto>>> groupChatList(@PathVariable("userId") String userId){
        List<GroupChatListResponseDto> chats = groupChartList.execute(userId);
        return new ResponseEntity<>(new ResponsePayload<>("chat list successfulllyl", chats), HttpStatus.OK);
    }

    //채팅 보내기
    @PostMapping("/{userId}")
    public ResponseEntity<ResponsePayload<GroupChatContentDomain>> sendChat(@RequestBody SendChatRequestDto requestDto){
        GroupChatContentDomain content = sendGroupChatService.execute(requestDto);
        return new ResponseEntity<>(new ResponsePayload<>("chat send successfully", content), HttpStatus.CREATED);
    }
}
