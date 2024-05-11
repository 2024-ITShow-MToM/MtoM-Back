package com.MtoM.MtoM.domain.user.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Builder
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity (name = "users")
public class UserDomain {
    @Id
    @Column
    private String id;

    @Column
    private String name;

    @Column
    private String birthday;

    @Column
    @Enumerated(EnumType.STRING)
    private Gender  gender;

    @Column(unique = true)
    private String phonenumber;

    @Column
    @Enumerated(EnumType.STRING)
    private Major major;

    @JsonIgnore
    @Column
    private String password;

    @Column(unique = true)
    private String email;

    @Column
    private String profile;

    @Column
    private String mbti;

    @Column
    private String content;

    @Column
    private String personal;

    @Column
    private String imogi;

    @Column
    private String social;
}
