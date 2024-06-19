package com.MtoM.MtoM.domain.groupChat.dto.req;

import com.MtoM.MtoM.domain.groupChat.domain.GroupChatContentDomain;
import com.MtoM.MtoM.domain.groupChat.domain.GroupChatDomain;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SendChatRequestDto {
    private Long projectId;
    private String content;

    public GroupChatContentDomain toEntity(GroupChatDomain groupChat) {
        return GroupChatContentDomain.builder()
                .groupChat(groupChat)
                .contents(content)
                .build();
    }
}