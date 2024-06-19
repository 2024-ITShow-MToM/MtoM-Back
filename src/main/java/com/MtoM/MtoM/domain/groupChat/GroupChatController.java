package com.MtoM.MtoM.domain.groupChat;

import com.MtoM.MtoM.domain.chat.dto.GroupChatListResponseDto;
import com.MtoM.MtoM.domain.groupChat.service.GroupChartList;
import com.MtoM.MtoM.global.message.ResponsePayload;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chats/groups")
public class GroupChatController {
    private final GroupChartList groupChartList;

    //나의 채팅 내역 불러오기
    @GetMapping("/list/{userId}")
    public ResponseEntity<ResponsePayload<List<GroupChatListResponseDto>>> groupChatList(@PathVariable("userId") String userId){
        List<GroupChatListResponseDto> chats = groupChartList.execute(userId);
        return new ResponseEntity<>(new ResponsePayload<>("chat list successfulllyl", chats), HttpStatus.OK);
    }
}
