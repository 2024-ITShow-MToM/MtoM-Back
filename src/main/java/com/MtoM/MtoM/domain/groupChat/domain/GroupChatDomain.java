package com.MtoM.MtoM.domain.groupChat.domain;

import com.MtoM.MtoM.domain.mentor.domain.MentorDomain;
import com.MtoM.MtoM.domain.project.domain.ProjectDomain;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "group_chats")
public class GroupChatDomain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private ProjectDomain project;

    @JsonIgnore
    @OneToMany(mappedBy = "groupChat", cascade = {CascadeType.REFRESH, CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST}, orphanRemoval = true)
    private List<GroupChatContentDomain> groupChatContent;;
}
