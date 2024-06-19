package com.MtoM.MtoM.domain.groupChat.dto.res;

import com.MtoM.MtoM.domain.groupChat.domain.GroupChatDomain;
import com.MtoM.MtoM.domain.groupChat.service.GroupChartList;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.query.sql.internal.ParameterRecognizerImpl;
import org.springframework.data.repository.query.Param;

import java.time.LocalTime;

@Getter @Setter
public class GroupChatListResponseDto {
    private Long projectId;
    private String name;
    private String profile;


    public GroupChatListResponseDto(GroupChatDomain chats){
        this.projectId = chats.getProject().getId();
        this.name = chats.getProject().getUser().getName();
        this.profile = chats.getProject().getImg();
    }
}
