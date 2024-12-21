package com.join.lesson.core.domain;

import com.join.lesson.core.entity.web.UserEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class User {

    private Long id;
    private String loginId;
    private String loginPw;
    private String name;
    private String tel;
    private String profileUrl;
    private String comment;
    private String role; // ROLE_MEMBER, ROLE_PRO
    private int active;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    @Builder
    public User(Long id, String loginId, String loginPw, String name, String tel, String profileUrl, String comment, String role, int active, LocalDateTime createdDate, LocalDateTime modifiedDate) {
        this.id = id;
        this.loginId = loginId;
        this.loginPw = loginPw;
        this.name = name;
        this.tel = tel;
        this.profileUrl = profileUrl;
        this.comment = comment;
        this.role = role;
        this.active = active;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }

    public UserEntity toEntity() {
        return UserEntity.builder()
                .loginId(this.getLoginId())
                .loginPw(this.getLoginPw())
                .name(this.getName())
                .tel(this.getTel())
                .profileUrl(this.getProfileUrl())
                .comment(this.getComment())
                .role(this.getRole())
                .active(this.getActive())
                .build();
    }
}
