package com.MtoM.MtoM.domain.groupChat.dto.req;

import com.MtoM.MtoM.domain.groupChat.domain.GroupChatContentDomain;
import com.MtoM.MtoM.domain.groupChat.domain.GroupChatDomain;
import com.MtoM.MtoM.domain.user.domain.UserDomain;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SendChatRequestDto {
    private Long projectId;
    private String content;

    public GroupChatContentDomain toEntity(GroupChatDomain groupChat, UserDomain user) {
        return GroupChatContentDomain.builder()
                .user(user)
                .groupChat(groupChat)
                .contents(content)
                .build();
    }
}